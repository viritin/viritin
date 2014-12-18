package org.vaadin.maddon.fields;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.vaadin.maddon.ListContainer;

/**
 * A Table that can be used as a field to modify List&gt;YourDomainObject&lt; or
 * List&gt;YourDomainObject&lt; typed fields in your domain model.
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
            Set oldvalue = (Set) getValue();
            super.setValue(newValue, repaintIsNotNeeded);
            if (clientSideChange) {
                Collection collection = getEditedCollection();
                Set newvalue = (Set) getValue();
                Set orphaned = new HashSet(oldvalue);
                orphaned.removeAll(newvalue);
                collection.removeAll(orphaned);
                Set newValues = new LinkedHashSet(newvalue);
                newValues.removeAll(oldvalue);
                collection.addAll(newValues);
            }
        }
        
    };
    
    private Collection<ET> getEditedCollection() {
        Collection c = getValue();
        if (c == null) {
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
                } catch (Exception ex) {
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
        return table.size() != 0;
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
            table.setContainerDataSource(new ListContainer(list));
        } else {
            table.setContainerDataSource(new ListContainer(list), Arrays.asList(
                    visProps));
        }
        return this;
    }
    
    public MultiSelectTable() {
        setHeight("300px");
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
     * @return the underlaying table implementation. Protected as one should
     * expect odd behavior if it configured in unsupported manner.
     */
    protected Table getUndelayingTable() {
        return table;
    }
    
    
}
