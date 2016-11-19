/*
 * Copyright 2014 Matti Tahvonen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.viritin;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeNotifier;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.AbstractContainer;
import org.apache.commons.beanutils.*;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.apache.commons.beanutils.expression.Resolver;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A replacement for BeanItemContainer from the core
 * <p>
 * The ListContainer is rather similar to the cores BeanItemContainer, but has
 * better typed API, much smaller memory overhead (practically no overhead if
 * data is given as List) and also otherwise better performance.
 *
 * @param <T> the type of beans in the backed list
 */
public class ListContainer<T> extends AbstractContainer implements
        Container.Indexed, Container.Sortable, ItemSetChangeNotifier {

    private static final long serialVersionUID = -6709228455051205922L;

    private List<T> backingList;
    private List<String> properties;

    public ListContainer(Collection<? extends T> backingList) {
        setCollection(backingList);
    }

    public ListContainer(Class<? extends T> type, Collection<? extends T> backingList) {
        if (type != null) {
            dynaClass = WrapDynaClass.createDynaClass(type);
        }
        setCollection(backingList);
    }

    public final void setCollection(Collection<? extends T> backingList1) {
        if (backingList1 instanceof List<?>) {
            this.backingList = (List<T>) backingList1;
        } else {
            this.backingList = new ArrayList<T>(backingList1); // Type parameter to keep NB happy
        }
        
        fireItemSetChange();
    }

    public ListContainer(Class<? extends T> type) {
        backingList = new ArrayList<>();
        dynaClass = WrapDynaClass.createDynaClass(type);
    }

    public ListContainer(Class<? extends T> type, String... properties) {
        this(type);
        setContainerPropertyIds(properties);
    }
    
    public ListContainer(DynaClass type) {
        backingList = new ArrayList<>();
        dynaClass = type;
    }

    public ListContainer(DynaClass type, String... properties) {
        this(type);
        setContainerPropertyIds(properties);
    }

    protected List<T> getBackingList() {
        return backingList;
    }

    private transient DynaClass dynaClass;

    private DynaClass getDynaClass() {
        if (dynaClass == null && !backingList.isEmpty()) {
            return getDynaClass(backingList.get(0));
        }
        return dynaClass;
    }

    private DynaClass getDynaClass(Object reference) {
        if (dynaClass == null && reference != null) {
        	if(reference instanceof DynaBean){
        		dynaClass = ((DynaBean)reference).getDynaClass();
        	}else{
        		dynaClass = WrapDynaClass.createDynaClass(reference.getClass());
        	}
        }
        return dynaClass;
    }

    @Override
    public int indexOfId(Object itemId) {
        return getBackingList().indexOf(itemId);
    }

    public int indexOf(T bean) {
        return indexOfId(bean);
    }

    @Override
    public T getIdByIndex(int index) {
        return getBackingList().get(index);
    }

    @Override
    public List<T> getItemIds(int startIndex, int numberOfItems) {
        // Whooo!? Vaadin calls this method with numberOfItems == -1
        if (numberOfItems < 0) {
            throw new IllegalArgumentException();
        }

        return getBackingList().subList(startIndex, startIndex + numberOfItems);
    }

    @Override
    public Object addItemAt(int index) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item addItemAt(int index, Object newItemId) throws UnsupportedOperationException {
        backingList.add(index, (T) newItemId);
        fireItemSetChange();
        return getItem(newItemId);
    }

    @Override
    public T nextItemId(Object itemId) {
        int i = getBackingList().indexOf(itemId) + 1;
        if (getBackingList().size() == i) {
            return null;
        }
        return getBackingList().get(i);
    }

    @Override
    public T prevItemId(Object itemId) {
        int i = getBackingList().indexOf(itemId) - 1;
        if (i < 0) {
            return null;
        }
        return getBackingList().get(i);
    }

    @Override
    public T firstItemId() {
        return (getBackingList().isEmpty()) ? null : getBackingList().get(0);
    }

    @Override
    public T lastItemId() {
        return getBackingList().isEmpty() ? null : getBackingList().get(
                getBackingList().size() - 1);
    }

    @Override
    public boolean isFirstId(Object itemId) {
        return itemId.equals(firstItemId());
    }

    @Override
    public boolean isLastId(Object itemId) {
        return itemId.equals(lastItemId());
    }

    @Override
    public Object addItemAfter(Object previousItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item addItemAfter(Object previousItemId, Object newItemId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Item getItem(Object itemId) {
        if (itemId == null) {
            return null;
        }
        return new DynaBeanItem<>((T) itemId);
    }

    @Override
    public Collection<String> getContainerPropertyIds() {
        if (properties == null) {
            if (getDynaClass() != null) {
                ArrayList<String> props = new ArrayList<>();
                for (DynaProperty db : getDynaClass().getDynaProperties()) {
                    if (db.getType() != null) {
                        props.add(db.getName());
                    } else {
                        // type may be null in some cases
                        Logger.getLogger(ListContainer.class.getName()).log(
                                Level.FINE, "Type not detected for property {0}",
                                db.getName());
                    }
                }
                props.remove("class");
                properties = props;
            } else {
                return Collections.EMPTY_LIST;
            }
        }
        return properties;

    }

    @Override
    public Collection<?> getItemIds() {
        return getBackingList();
    }

    @Override
    public Property getContainerProperty(Object itemId, Object propertyId) {
        return getItem(itemId).getItemProperty(propertyId);
    }

    @Override
    public Class<?> getType(Object propertyId) {
        final String pName = propertyId.toString();
        try {
            final DynaProperty dynaProperty = getDynaClass().getDynaProperty(
                    pName);
            final Class<?> type = dynaProperty.getType();
            if (type.isPrimitive()) {
                // Vaadin can't handle primitive types in _all_ places, so use
                // wrappers instead. FieldGroup works, but e.g. Table in _editable_
                // mode fails for some reason
                return ClassUtils.primitiveToWrapper(type);
            }
            return type;
        } catch (Exception e) {
            // If type can't be found via dynaClass, it is most likely 
            // nested/indexed/mapped property
            try {
                return getNestedPropertyType(getDynaClass(), pName);
            } catch (final IllegalAccessException 
                         | InvocationTargetException 
                         | NoSuchMethodException 
                         | ClassNotFoundException 
                         | NoSuchFieldException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static Resolver resolver = new DefaultResolver();

    public static Class<?> getNestedPropertyType(DynaClass bean, String name)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, ClassNotFoundException, NoSuchFieldException {
        if (bean == null) {
            // The type is not properly initilized yet, just leave it as generic
            // Object
            return Object.class;
        }
        // Resolve nested references
        while (resolver.hasNested(name)) {
            String next = resolver.next(name);
            if (resolver.isIndexed(next) || resolver.isMapped(next)) {
                String property = resolver.getProperty(next);
                Class<?> clazz = Class.forName(bean.getName());
                Class<?> detectTypeParameter = detectTypeParameter(clazz,
                        property, resolver.isIndexed(name) ? 0 : 1);
                bean = WrapDynaClass.createDynaClass(detectTypeParameter);
                return getNestedPropertyType(bean, resolver.remove(name));
            }
            DynaProperty db = bean.getDynaProperty(next);
            bean = WrapDynaClass.createDynaClass(db.getType());
            name = resolver.remove(name);
        }
        if (resolver.isMapped(name) || resolver.isIndexed(name)) {
            String property = resolver.getProperty(name);
            Class<?> clazz = Class.forName(bean.getName());
            return detectTypeParameter(clazz, property,
                    resolver.isIndexed(name) ? 0 : 1);
        }
        Class<?> type;
        DynaProperty dynaProperty = bean.getDynaProperty(name);
        if (dynaProperty != null) {
        	type = dynaProperty.getType();
        } 
        else { //happens for default methods
        	Method method = obtainGetterOfProperty(bean, name);
        	type = method.getReturnType();
        }
        if (type.isPrimitive() == true) {
            // Vaadin can't handle primitive types in _all_ places, so use
            // wrappers instead. FieldGroup works, but e.g. Table in _editable_
            // mode fails for some reason
            return ClassUtils.primitiveToWrapper(type);
        }
        return type;
    }
    
    /**
     * Explicitly resolves the getter bypassing commons.beanutils
     * @param beanClass
     * @param propertyName
     * @return getter method of the property
     * @throws ClassNotFoundException if class forName fails
     * @throws NoSuchMethodException if there is no getter
     * @throws SecurityException if there are constraints by the security manager
     */
    private static Method obtainGetterOfProperty(DynaClass beanClass, String propertyName) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
    	Class<?> clazz = Class.forName(beanClass.getName());
    	return clazz.getMethod("get"+firstLetterUppercase(propertyName));
    }
    private static String firstLetterUppercase(String s) {
    	return Character.toUpperCase(s.charAt(0)) + (s.length() > 1 ? s.substring(1) : "");
    }

    private static Class<?> detectTypeParameter(Class clazz, String name,
            int idx) throws NoSuchFieldException {
        Field declaredField = clazz.getDeclaredField(name);
        Type genericType = declaredField.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return (Class<?>) parameterizedType.getActualTypeArguments()[idx];
        }
        return Object.class;
    }

    @Override
    public int size() {
        return getBackingList().size();
    }

    @Override
    public boolean containsId(Object itemId) {
        return getBackingList().contains(itemId);
    }

    @Override
    public Item addItem(Object itemId) throws UnsupportedOperationException {
        backingList.add((T) itemId);
        fireItemSetChange();
        return getItem(itemId);
    }

    @Override
    public Object addItem() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeItem(Object itemId) throws UnsupportedOperationException {
        final boolean remove = backingList.remove(itemId);
        if (remove) {
            fireItemSetChange();
        }
        return remove;
    }

    @Override
    public boolean addContainerProperty(Object propertyId,
            Class<?> type, Object defaultValue) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean removeAllItems() throws UnsupportedOperationException {
        backingList.clear();
        fireItemSetChange();
        return true;
    }

    public ListContainer addAll(Collection<T> beans) {
        backingList.addAll(beans);
        fireItemSetChange();
        return this;
    }

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) {

        // Grid in 7.4 may call this method with empty sorting instructions...
        if (propertyId.length > 0) {
            if (backingList instanceof SortableLazyList) {
                // using with MGrid may end up here
                SortableLazyList sll = (SortableLazyList) backingList;
                String[] stringProperties = new String[propertyId.length];
                System.arraycopy(propertyId, 0, stringProperties, 0, propertyId.length);
                sll.setSortProperty(stringProperties);
                sll.setSortAscending(ascending);
                sll.reset();
            } else {
                Comparator<T> comparator = new PropertyComparator(propertyId,
                        ascending);

                Collections.sort(backingList, comparator);
            }
            fireItemSetChange();
        }
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        if (backingList instanceof SortableLazyList) {
            // Assume SortableLazyList can sort by any Comparable property
        } else if (backingList instanceof LazyList) {
            // When using LazyList, don't support sorting by default
            // as the sorting should most probably be done at backend call level
            return Collections.emptySet();
        }
        final ArrayList<String> props = new ArrayList<>();
        for (Object a : getContainerPropertyIds()) {
            String propName = a.toString();
            try {
                Class<?> propType = getType(propName);
                if (propType != null && (propType.isPrimitive() || Comparable.class.isAssignableFrom(propType))) {
                    props.add(propName);
                }
            } catch (Exception e) {
            }
        }
        return props;
    }

    @Override
    public void addItemSetChangeListener(
            Container.ItemSetChangeListener listener) {
        super.addItemSetChangeListener(listener);
    }

    @Override
    public void removeItemSetChangeListener(
            Container.ItemSetChangeListener listener) {
        super.removeItemSetChangeListener(listener);
    }

    @Override
    public void addListener(Container.ItemSetChangeListener listener) {
        super.addListener(listener);
    }

    @Override
    public void removeListener(Container.ItemSetChangeListener listener) {
        super.removeListener(listener);
    }

    /**
     *
     * Override point. Allows user to use custom comparators based on
     * properties.
     *
     * @param property the property whose comparator is requested
     * @return Comparator that will compare two objects based on a property
     */
    protected Comparator<T> getUnderlyingComparator(Object property) {
        return new NullComparator();
    }

    /**
     * Explicitly sets the property ids that should be reported by this
     * container. Allows e.g. usage of "nested properties".
     *
     * @param properties the properties reported by this container
     */
    public void setContainerPropertyIds(String... properties) {
        this.properties = Arrays.asList(properties);
    }

    @Override
    public void fireItemSetChange() {
        super.fireItemSetChange();
    }

    private class PropertyComparator implements Comparator<T> {

        private final Object[] propertyId;
        private final boolean[] ascending;

        private PropertyComparator(Object[] propertyId, boolean[] ascending) {
            this.propertyId = propertyId;
            this.ascending = ascending;
        }

        @Override
        public int compare(T o1, T o2) {
            for (int i = 0; i < propertyId.length; i++) {
                String currentProperty = propertyId[i].toString();
                Comparator underlyingComparator = getUnderlyingComparator(currentProperty);
                Comparator currentComparator = underlyingComparator != null ? underlyingComparator : ComparableComparator.getInstance();

                if (!ascending[i]) {
                    currentComparator = new ReverseComparator(currentComparator);
                }

                Object o1Prop = getContainerProperty(o1, currentProperty).getValue();
                Object o2Prop = getContainerProperty(o2, currentProperty).getValue();
                int compare = currentComparator.compare(o1Prop, o2Prop);
                if (compare != 0) {
                    return compare;
                }
            }

            return 0;
        }
    }

    public class DynaBeanItem<T> implements Item {

        private static final long serialVersionUID = 39911097876284908L;

        private final Map<Object, DynaProperty> propertyIdToProperty = new HashMap<>();

        private class DynaProperty implements Property {

            private static final long serialVersionUID = -6887983770952190177L;

            private final String propertyName;

            DynaProperty(String property) {
                propertyName = property;
            }

            @Override
            public Object getValue() {
            	DynaBean dynaBean = getDynaBean();
            	try {
                    return dynaBean.get(propertyName);
                } catch (final Exception e) {
                    try {
                        return PropertyUtils.getProperty(bean, propertyName);
                    } 
                    catch (final NestedNullException | java.lang.IndexOutOfBoundsException ex) {
                        return null;
                    } catch (final IllegalAccessException | InvocationTargetException ex) {
                        throw new RuntimeException(ex);
                    } catch (final NoSuchMethodException ex) {
                    	Logger.getLogger(ListContainer.class.getName()).log(
                                Level.FINE, "Trying default method fallback for property {0}",
                                propertyName);
                    }
                }
            	
            	//fallback for default methods:
            	Method method;
				try {
					method = obtainGetterOfProperty(dynaBean.getDynaClass(), propertyName);
					return method.invoke(bean);
				} catch (final ReflectiveOperationException 
                             | SecurityException 
                             | IllegalArgumentException e) {
					throw new RuntimeException(e);
				}
            }

            @Override
            public void setValue(Object newValue) throws Property.ReadOnlyException {
                try {
                    PropertyUtils.setProperty(bean, propertyName, newValue);
                } catch (final IllegalAccessException 
                             | InvocationTargetException 
                             | NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public Class getType() {
                return ListContainer.this.getType(propertyName);
            }

            @Override
            public boolean isReadOnly() {
            	DynaClass clazz = getDynaClass();
            	if(clazz instanceof WrapDynaClass){
            		return ((WrapDynaClass)clazz).getPropertyDescriptor(propertyName).
            				getWriteMethod() == null;
            	}
                
            	return false;
            }

            @Override
            public void setReadOnly(boolean newStatus) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        }

        private final T bean;

        private transient DynaBean db;

        public DynaBeanItem(T bean) {
            this.bean = bean;
        }

        public T getBean() {
            return bean;
        }

        private DynaBean getDynaBean() {
            if (db == null) {
                try {
                	if(bean instanceof DynaBean){
                		db = (DynaBean) bean;
                	}else{
                		db = new WrapDynaBean(bean, (WrapDynaClass) getDynaClass(bean));
                	}
                } catch (Throwable e) {
                    // Older version of beanutils is somehow available by the 
                    // classloader! Probably tomee
                    db = new WrapDynaBean(bean);
                }
            }
            return db;
        }

        @Override
        public Property getItemProperty(Object id) {
            DynaProperty prop = propertyIdToProperty.get(id);
            if (prop == null) {
                prop = new DynaProperty(id.toString());
                propertyIdToProperty.put(id, prop);
            }
            return prop;
        }

        @Override
        public Collection<String> getItemPropertyIds() {
            return ListContainer.this.getContainerPropertyIds();
        }

        @Override
        public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean removeItemProperty(Object id) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}