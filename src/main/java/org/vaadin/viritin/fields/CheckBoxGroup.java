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

import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.OptionGroup;

/**
 * An OptionGroup that can be used as a field to modify
 * List&gt;YourDomainObject&lt; or Set&gt;YourDomainObject&lt; typed fields in
 * your domain model.
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
public class CheckBoxGroup<ET> extends CustomField<Collection> {
    
    private CaptionGenerator<ET> captionGenerator;

    OptionGroup optionGroup = new OptionGroup() {

        {
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
            Set oldvalue = (Set) getValue();
            super.setValue(newValue, repaintIsNotNeeded);
            if (clientSideChange) {
                // TODO add strategies for maintaining the order in case of List
                // e.g. same as listing, selection order ...
                Collection collection = getEditedCollection();
                Set newvalue = (Set) getValue();
                Set orphaned = new HashSet(oldvalue);
                orphaned.removeAll(newvalue);
                collection.removeAll(orphaned);
                Set newValues = new LinkedHashSet(newvalue);
                newValues.removeAll(oldvalue);
                collection.addAll(newValues);
                CheckBoxGroup.this.fireValueChange(true);
            }
        }

        @Override
        public String getItemCaption(Object itemId) {
            if(captionGenerator != null) {
                return captionGenerator.getCaption((ET) itemId);
            }
            return super.getCaption();
        }

    };

    public CheckBoxGroup(String caption) {
        this();
        setCaption(caption);
    }
    
    public CheckBoxGroup<ET> setCaptionGenerator(CaptionGenerator<ET> captionGenerator) {
        this.captionGenerator = captionGenerator;
        return this;
    }

    private Collection<ET> getEditedCollection() {
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

    @Override
    protected Component initContent() {
        return optionGroup;
    }

    @Override
    public Class<? extends Collection> getType() {
        return Collection.class;
    }

    @Override
    protected void setInternalValue(Collection newValue) {
        super.setInternalValue(newValue);
        optionGroup.setValue(newValue);
    }

    /**
     * Sets the list of options available.
     *
     * @param list the list of available options
     * @return this for fluent configuration
     */
    public CheckBoxGroup<ET> setOptions(List<ET> list) {
        optionGroup.setContainerDataSource(new ListContainer(list));
        return this;
    }

    /**
     * Sets the list of options available.
     *
     * @param list the list of available options
     * @return this for fluent configuration
     */
    public CheckBoxGroup<ET> setOptions(ET... list) {
        return setOptions(Arrays.asList(list));
    }

    public CheckBoxGroup() {
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
        if (!optionGroup.isSelected(objectToSelect)) {
            optionGroup.select(objectToSelect);
            getEditedCollection().add(objectToSelect);
        }
    }

    public void unSelect(ET objectToDeselect) {
        if (optionGroup.isSelected(objectToDeselect)) {
            optionGroup.unselect(objectToDeselect);
            getEditedCollection().remove(objectToDeselect);
        }
    }

    /**
     * @return the underlaying table implementation. Protected as one should
     * expect odd behavior if it configured in unsupported manner.
     */
    protected OptionGroup getUndelayingTable() {
        return optionGroup;
    }

    public CheckBoxGroup<ET> withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    public CheckBoxGroup<ET> withHeight(String height) {
        setHeight(height);
        return this;
    }

    public CheckBoxGroup<ET> withFullHeight() {
        return withHeight("100%");
    }

    public CheckBoxGroup<ET> withWidth(String width) {
        setWidth(width);
        return this;
    }

    public CheckBoxGroup<ET> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public CheckBoxGroup<ET> withId(String id) {
        setId(id);
        return this;
    }

}
