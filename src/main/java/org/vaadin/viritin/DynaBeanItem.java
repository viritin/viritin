package org.vaadin.viritin;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.WrapDynaBean;

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
 * @param <T>
 */
public class DynaBeanItem<T> implements Item {

    /* Container-Item-Property specifications don't say item should always return
     the same property instance, but some components depend on this :-(
     */
    private Map<Object, DynaProperty> propertyIdToProperty = new HashMap<Object, DynaProperty>();

    private class DynaProperty implements Property {

        private final String propertyName;

        public DynaProperty(String property) {
            propertyName = property;
        }

        @Override
        public Object getValue() {
            return getDynaBean().get(propertyName);
        }

        @Override
        public void setValue(Object newValue) throws Property.ReadOnlyException {
            getDynaBean().set(propertyName, newValue);
        }

        @Override
        public Class getType() {
            return getDynaBean().getDynaClass().getDynaProperty(propertyName).
                    getType();
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

    private T bean;

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
        if (getDynaBean().getDynaClass().getDynaProperty(id.toString()) == null) {
            // Lazy query container detects some debug properties via 
            // Item!! 
            return null;
        }
        DynaProperty prop = propertyIdToProperty.get(id);
        if (prop == null) {
            prop = new DynaProperty(id.toString());
            propertyIdToProperty.put(id, prop);
        }
        return prop;
    }

    @Override
    public Collection<String> getItemPropertyIds() {
        ArrayList<String> properties = new ArrayList();
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
