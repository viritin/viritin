package org.vaadin.viritin.grid;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.PropertyValueGenerator;
import org.vaadin.viritin.ListContainer;

import java.util.*;

/**
 *
 * @author Shabak Nikolay (nikolay.shabak@gmail.com)
 * @since 23.04.2016
 * @param <T> the entity type listed in the consumer of the container, Vaadin Grid
 */
public class GeneratedPropertyListContainer<T> extends ListContainer<T> {

    private final Map<String, PropertyValueGenerator<?>> propertyGenerators = new HashMap();
    protected final Class<T> type;

    /**
     * Property implementation for generated properties
     */
    protected static class GeneratedProperty<T> implements Property<T>  {

        private Item item;
        private Object itemId;
        private Object propertyId;
        private PropertyValueGenerator<T> generator;

        public GeneratedProperty(Item item, Object propertyId, Object itemId,
                                 PropertyValueGenerator<T> generator) {
            this.item = item;
            this.itemId = itemId;
            this.propertyId = propertyId;
            this.generator = generator;
        }

        @Override
        public T getValue() {
            return generator.getValue(item, itemId, propertyId);
        }

        @Override
        public void setValue(T newValue) throws ReadOnlyException {
            throw new ReadOnlyException("Generated properties are read only");
        }

        @Override
        public Class<? extends T> getType() {
            return generator.getType();
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }

        @Override
        public void setReadOnly(boolean newStatus) {
            if (newStatus) {
                // No-op
                return;
            }
            throw new UnsupportedOperationException(
                    "Generated properties are read only");
        }
    }

    /**
     * Item implementation for generated properties.
     */
    protected class GeneratedPropertyItem implements Item {

        private Item wrappedItem;
        private Object itemId;

        protected GeneratedPropertyItem(Object itemId, Item item) {
            this.itemId = itemId;
            wrappedItem = item;
        }

        @Override
        public Property getItemProperty(Object id) {
            if (propertyGenerators.containsKey(id)) {
                return createProperty(wrappedItem, id, itemId,
                        propertyGenerators.get(id));
            }
            return wrappedItem.getItemProperty(id);
        }

        @Override
        public Collection<?> getItemPropertyIds() {
            Set wrappedProperties = new HashSet<>(wrappedItem.getItemPropertyIds());
            wrappedProperties.addAll(propertyGenerators.keySet());
            return wrappedProperties;
        }

        @Override
        public boolean addItemProperty(Object id, Property property)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException(
                    "GeneratedPropertyItem does not support adding properties");
        }

        @Override
        public boolean removeItemProperty(Object id)
                throws UnsupportedOperationException {
            throw new UnsupportedOperationException(
                    "GeneratedPropertyItem does not support removing properties");
        }

        /**
         * Tests if the given object is the same as the this object. Two Items
         * from the same container with the same ID are equal.
         *
         * @param obj
         *            an object to compare with this object
         * @return <code>true</code> if the given object is the same as this
         *         object, <code>false</code> if not
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (obj == null
                    || !obj.getClass().equals(GeneratedPropertyItem.class)) {
                return false;
            }
            final GeneratedPropertyItem li = (GeneratedPropertyItem) obj;
            return getContainer() == li.getContainer()
                    && itemId.equals(li.itemId);
        }

        @Override
        public int hashCode() {
            return itemId.hashCode();
        }

        private GeneratedPropertyListContainer getContainer() {
            return GeneratedPropertyListContainer.this;
        }
    }

    public GeneratedPropertyListContainer(Class<T> type) {
        super(type);
        this.type = type;
    }

    public GeneratedPropertyListContainer(Class<T> type, String... properties) {
        super(type);
        this.type = type;
        setContainerPropertyIds(properties);
    }

    public void addGeneratedProperty(String propertyId, PropertyValueGenerator<?> generator) {
        propertyGenerators.put(propertyId, generator);
        fireContainerPropertySetChange();
    }

    /**
     * @param <P> the presentation type, displays the generated value
     * @param propertyId the property id for generated property
     * @param presentationType the presentation type of the generated property
     * @param generator the generator that creates the property value on demand
     */
    public <P> void addGeneratedProperty(String propertyId,
                                         Class<P> presentationType,
                                         TypedPropertyValueGenerator.ValueGenerator<T, P> generator) {
        TypedPropertyValueGenerator<T, P> lambdaPropertyValueGenerator =
                new TypedPropertyValueGenerator<>(type, presentationType, generator);
        propertyGenerators.put(propertyId, lambdaPropertyValueGenerator);
        fireContainerPropertySetChange();
    }

    public void addGeneratedProperty(String propertyId,
                                     StringPropertyValueGenerator.ValueGenerator<T> generator) {
        StringPropertyValueGenerator<T> lambdaPropertyValueGenerator =
                new StringPropertyValueGenerator<>(type, generator);
        propertyGenerators.put(propertyId, lambdaPropertyValueGenerator);
        fireContainerPropertySetChange();
    }

    @Override
    public Class<?> getType(Object propertyId) {
        if (propertyGenerators.containsKey(propertyId)) {
            return propertyGenerators.get(propertyId).getType();
        }
        return super.getType(propertyId);
    }

    @Override
    public Item getItem(Object itemId) {
        if (itemId == null) {
            return null;
        }
        Item item = super.getItem(itemId);
        return createGeneratedPropertyItem(itemId, item);
    }

    private <T> Property<T> createProperty(final Item item,
                                           final Object propertyId, final Object itemId,
                                           final PropertyValueGenerator<T> generator) {
        return new GeneratedProperty<T>(item, propertyId, itemId, generator);
    }

    private Item createGeneratedPropertyItem(final Object itemId,
                                             final Item item) {
        return new GeneratedPropertyItem(itemId, item);
    }

}
