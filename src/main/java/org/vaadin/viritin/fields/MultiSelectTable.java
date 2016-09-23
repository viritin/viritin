package org.vaadin.viritin.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.vaadin.viritin.ListContainer;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Table;
import org.vaadin.viritin.MSize;

/**
 * A Table that can be used as a field to modify List&gt;YourDomainObject&lt; or
 * Set&gt;YourDomainObject&lt; typed fields in your domain model.
 * <p>
 * The field supports only non buffered mode. Also, the field tries to keep the
 * original collection and just modify its content. This helps e.g. some ORM
 * libraries to create more efficient updates to the database.
 * <p>
 * If the field value is null (and users selects rows) the field tries to add a
 * value with no-arg constructor or with ArrayList/HashMap if interface is used.
 *
 * @author Matti Tahvonen
 * @param <ET> The type in the entity collection
 */
public class MultiSelectTable<ET> extends CustomField<Collection> {

    private String[] visProps;
    private String[] pendingHeaders;

    Table table = new Table() {

        {
            setSelectable(true);
            setMultiSelect(true);
            setSizeFull();
            setImmediate(true);
        }

        private boolean clientSideChange;

        @Override
        public void changeVariables(Object source,
                Map<String, Object> variables) {
            clientSideChange = true;
            super.changeVariables(source, variables);
            clientSideChange = false;
        }

        @Override
        protected void setValue(Object newValue, boolean repaintIsNotNeeded) throws ReadOnlyException {
            Set<ET> oldvalue = (Set<ET>) getValue();
            super.setValue(newValue, repaintIsNotNeeded);
            if (clientSideChange) {
                // TODO add strategies for maintaining the order in case of List
                // e.g. same as listing, selection order ...
                Set newvalue = (Set) getValue();
                Set<ET> orphaned = new HashSet<>(oldvalue);
                orphaned.removeAll(newvalue);
                removeRelation(orphaned);
                allRemovedRelations.addAll(orphaned);
                allAddedRelations.removeAll(orphaned);
                Set<ET> newValues = new LinkedHashSet<>(newvalue);
                newValues.removeAll(oldvalue);
                addRelation(newValues);
                allAddedRelations.addAll(newValues);
                allRemovedRelations.removeAll(newValues);
                MultiSelectTable.this.fireValueChange(true);
            }
        }

    };
    private Class<ET> optionType;

    private Set<ET> allAddedRelations = new HashSet<>();
    private Set<ET> allRemovedRelations = new HashSet<>();

    public MultiSelectTable(Class<ET> optionType) {
        this();
        table.setContainerDataSource(new ListContainer(optionType));
        this.optionType = optionType;
    }

    public MultiSelectTable(String caption) {
        this();
        setCaption(caption);
    }

    /**
     * Adds given options to the modified collection. The default implementation
     * simply uses java.util.Collection APIs to maintain the edited collection.
     * If the underlying data model needs some some custom maintenance, e.g. a
     * bi-directional many-to-many relation in JPA, you can add your own logic
     * by overriding this method.
     *
     * @param newValues the new objects to be added to the collection.
     */
    protected void addRelation(Set<ET> newValues) {
        getEditedCollection().addAll(newValues);
    }

    /**
     * Removes given options from the modified collection. The default
     * implementation simply uses java.util.Collection APIs to maintain the
     * edited collection. If the underlying data model needs some some custom
     * maintenance, e.g. a bi-directional many-to-many relation in JPA, you can
     * add your own logic by overriding this method.
     *
     * @param orphaned the new objects to be removed from the collection.
     */
    protected void removeRelation(Set<ET> orphaned) {
        getEditedCollection().removeAll(orphaned);
    }

    /**
     * @return all objects that are added to the edited collection. The added
     * relations are calculated since last call to setPropertyDataSource method
     * (when the modified property/collection is set by e.g. FieldGroup) or
     * explicit call to clearModifiedRelations().
     */
    public Set<ET> getAllAddedRelations() {
        return allAddedRelations;
    }

    /**
     * @return all objects that are removed from the edited collection. The
     * deleted relations are calculated since last call to setPropertyDataSource
     * method (when the modified property/collection is set by e.g. FieldGroup)
     * or explicit call to clearModifiedRelations().
     */
    public Set<ET> getAllRemovedRelations() {
        return allRemovedRelations;
    }

    /**
     * @return all objects that are either added or removed to/from the edited
     * collection. The objects are calculated since last call to
     * setPropertyDataSource method (when the modified property/collection is
     * set by e.g. FieldGroup) or explicit call to clearModifiedRelations().
     */
    public Set<ET> getAllModifiedRelations() {
        HashSet<ET> all = new HashSet<>(allAddedRelations);
        all.addAll(allRemovedRelations);
        return all;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        clearModifiedRelations();
        super.setPropertyDataSource(newDataSource);
    }

    /**
     * Clears the fields that track the elements that are added or removed to
     * the modified collection. This method is automatically called when a new
     * property is assigned to this field.
     */
    public void clearModifiedRelations() {
        allAddedRelations.clear();
        allRemovedRelations.clear();
    }

    /**
     * @return the collection being edited by this field.
     */
    protected Collection<ET> getEditedCollection() {
        Collection c = getValue();
        if (c == null) {
            if (getPropertyDataSource() == null) {
                // this should never happen :-)
                return new HashSet();
            }
            Class fieldType = getPropertyDataSource().getType();
            if (fieldType.isInterface()) {
                if (fieldType == List.class) {
                    c = new ArrayList();
                } else { // Set
                    c = new HashSet();
                }
            } else {
                try {
                    c = (Collection) fieldType.newInstance();
                } catch (IllegalAccessException | InstantiationException ex) {
                    throw new RuntimeException(
                            "Could not instantiate the used colleciton type", ex);
                }
            }
        }
        return c;
    }

    public MultiSelectTable<ET> withProperties(String... visibleProperties) {
        visProps = visibleProperties;
        if (isContainerInitialized()) {
            table.setVisibleColumns((Object[]) visibleProperties);
        } else {
            for (String string : visibleProperties) {
                table.addContainerProperty(string, String.class, "");
            }
        }
        return this;
    }

    private boolean isContainerInitialized() {
        return table.getContainerDataSource() instanceof ListContainer;
    }

    public MultiSelectTable<ET> withColumnHeaders(
            String... columnNamesForVisibleProperties) {
        if (isContainerInitialized()) {
            table.setColumnHeaders(columnNamesForVisibleProperties);
        } else {
            pendingHeaders = columnNamesForVisibleProperties;
            // Add headers to temporary indexed container, in case table is initially
            // empty
            for (String prop : columnNamesForVisibleProperties) {
                table.addContainerProperty(prop, String.class, "");
            }
        }
        return this;
    }

    @Override
    protected Component initContent() {
        return table;
    }

    @Override
    public Class<? extends Collection> getType() {
        return Collection.class;
    }

    @Override
    protected void setInternalValue(Collection newValue) {
        super.setInternalValue(newValue);
        table.setValue(newValue);
    }

    /**
     * Sets the list of options available.
     *
     * @param list the list of available options
     * @return this for fluent configuration
     */
    public MultiSelectTable<ET> setOptions(List<ET> list) {
        if (visProps == null) {
            table.setContainerDataSource(new ListContainer(optionType, list));
        } else {
            table.setContainerDataSource(new ListContainer(optionType, list), Arrays.asList(
                    visProps));
        }
        if (pendingHeaders != null) {
            table.setColumnHeaders(pendingHeaders);
        }
        return this;
    }

    /**
     * Sets the list of options available.
     *
     * @param list the list of available options
     * @return this for fluent configuration
     */
    public MultiSelectTable<ET> setOptions(ET... list) {
        if (visProps == null) {
            table.setContainerDataSource(new ListContainer(optionType, Arrays.
                    asList(list)));
        } else {
            table.setContainerDataSource(new ListContainer(optionType, Arrays.
                    asList(list)), Arrays.asList(
                    visProps));
        }
        if (pendingHeaders != null) {
            table.setColumnHeaders(pendingHeaders);
        }
        return this;
    }

    public MultiSelectTable() {
        setHeight("230px");
        // TODO verify if this is needed in real usage, but at least to pass the test
        setConverter(new Converter<Collection, Collection>() {

            @Override
            public Collection convertToModel(Collection value,
                    Class<? extends Collection> targetType, Locale locale) throws Converter.ConversionException {
                return value;
            }

            @Override
            public Collection convertToPresentation(Collection value,
                    Class<? extends Collection> targetType, Locale locale) throws Converter.ConversionException {
                return value;
            }

            @Override
            public Class<Collection> getModelType() {
                return (Class<Collection>) getEditedCollection().getClass();
            }

            @Override
            public Class<Collection> getPresentationType() {
                return Collection.class;
            }

        });
    }

    public void select(ET objectToSelect) {
        if (!table.isSelected(objectToSelect)) {
            table.select(objectToSelect);
            getEditedCollection().add(objectToSelect);
        }
    }

    public void unSelect(ET objectToDeselect) {
        if (table.isSelected(objectToDeselect)) {
            table.unselect(objectToDeselect);
            getEditedCollection().remove(objectToDeselect);
        }
    }

    /**
     * @return the underlaying Table
     * @deprecated use getTable() instead.
     */
    @Deprecated
    protected Table getUnderlayingTable() {
        return table;
    }

    /**
     * @return the underlaying table implementation. Note that the component
     * heavily relies on some features so changing some of the configuration
     * options in Table is unsafe.
     */
    public Table getTable() {
        return table;
    }

    public MultiSelectTable<ET> withColumnHeaderMode(Table.ColumnHeaderMode mode) {
        getUnderlayingTable().setColumnHeaderMode(mode);
        return this;
    }

    public MultiSelectTable<ET> withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    public MultiSelectTable<ET> withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MultiSelectTable<ET> withFullHeight() {
        return withHeight("100%");
    }

    public MultiSelectTable<ET> withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MultiSelectTable withSize(MSize mSize) {
        setWidth(mSize.getWidth(), mSize.getWidthUnit());
        setHeight(mSize.getHeight(), mSize.getHeightUnit());
        return this;
    }

    public MultiSelectTable<ET> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MultiSelectTable<ET> withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MultiSelectTable<ET> withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MultiSelectTable<ET> withId(String id) {
        table.setId(id);
        return this;
    }

    public MultiSelectTable<ET> expand(String... propertiesToExpand) {
        for (String property : propertiesToExpand) {
            table.setColumnExpandRatio(property, 1);
        }
        return this;
    }

    public void withRowHeaderMode(Table.RowHeaderMode rowHeaderMode) {
        getUnderlayingTable().setRowHeaderMode(rowHeaderMode);
    }

    public MultiSelectTable<ET> setMultiSelectMode(MultiSelectMode mode) {
        getTable().setMultiSelectMode(mode);
        return this;
    }

}
