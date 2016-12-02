package org.vaadin.viritin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.apache.commons.lang3.ClassUtils;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

/**
 * A standalone version of the DynaBeanItem originally introduced in
 * ListContainer. Can be used to implement e.g. efficient LazyQueryContainer
 * instances for service layers.
 *
 * TODO check if this could be used in ListContainer without performance
 * drawbacks.
 *
 * TODO check if some staff could be abstracted away
 *
 * @author Matti Tahvonen
 * @param <T> the type of the bean wrapped by the item
 */
public class DynaBeanItem<T> implements Item {

    private static final long serialVersionUID = -5073690046197951234L;

    /* Container-Item-Property specifications don't say item should always return
     the same property instance, but some components depend on this :-(
     */
    private final Map<Object, DynaProperty> propertyIdToProperty = new HashMap<>();

    private class DynaProperty implements Property {

        private static final long serialVersionUID = -2419540615310696644L;

        private final String propertyName;

        DynaProperty(String property) {
            propertyName = property;
        }

        @Override
        public Object getValue() {
            try {
                return getDynaBean().get(propertyName);
            } catch (Exception e) {
                try {
                    return PropertyUtils.getProperty(bean, propertyName);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        @Override
        public void setValue(Object newValue) throws Property.ReadOnlyException {
            getDynaBean().set(propertyName, newValue);
        }

        @Override
        public Class<?> getType() {
            try {
                final org.apache.commons.beanutils.DynaProperty dynaProperty = getDynaBean().
                        getDynaClass().
                        getDynaProperty(
                                propertyName);
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
                    return org.vaadin.viritin.ListContainer.
                            getNestedPropertyType(getDynaBean().getDynaClass(),
                                    propertyName);
                } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            // Silently ignore, e.g. LazyQueryContainer calls this
            // throw new UnsupportedOperationException("Not supported yet.");
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
            db = new WrapDynaBean(bean);
        }
        return db;
    }

    @Override
    public Property getItemProperty(Object id) {
        final String propertyName = id.toString();
        if (getDynaBean().getDynaClass().getDynaProperty(propertyName) == null) {
            DefaultResolver defaultResolver = new DefaultResolver();
            if (!(defaultResolver.hasNested(propertyName) || defaultResolver.
                    isIndexed(propertyName) || defaultResolver.hasNested(
                    propertyName))) {
                // Lazy query container detects some debug properties via 
                // Item!! 
                return null;
            }
        }
        DynaProperty prop = propertyIdToProperty.get(id);
        if (prop == null) {
            prop = new DynaProperty(propertyName);
            propertyIdToProperty.put(id, prop);
        }
        return prop;
    }

    @Override
    public Collection<String> getItemPropertyIds() {
        ArrayList<String> properties = new ArrayList<String>();
        for (org.apache.commons.beanutils.DynaProperty dp : getDynaBean().
                getDynaClass().getDynaProperties()) {
            properties.add(dp.getName());
        }
        properties.remove("class");
        return properties;
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
