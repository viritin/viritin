package org.vaadin.maddon.fields;

import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TwinColSelect;
import java.util.Arrays;
import org.vaadin.maddon.ListContainer;

/**
 * A select implementation with better typed API than in core Vaadin.
 *
 * By default the options toString is used to generate the caption for option.
 * To override this behavior, use setCaptionGenerator or override getCaption(T)
 * to provide your own strategy.
 * <p>
 * Behind the scenes uses Vaadin cores NativeSelect (default) or other cores
 * AbstractSelect implementation, type provided in constructor. Tree and Table
 * are not supported, see MTable.
 * <p>
 * Note, that this select is always in single select mode. See CollectionSelect
 * for "multiselect".
 *
 * @author mstahv
 * @param <T> the type of selects value
 */
public class TypedSelect<T> extends CustomComponent implements Field<T> {
    
    private CaptionGenerator<T> captionGenerator;
    
    private AbstractSelect select;
    
    private ListContainer<T> bic;
    
    private Class<T> fieldType;
    
    public TypedSelect(Class<T> type) {
        this.fieldType = type;
        bic = new ListContainer<T>(type);
    }

    /**
     * Note, that with this constructor, you cannot override the select type.
     * @param options options to select from
     */
    public TypedSelect(T... options) {
        setOptions(options);
    }
    
    public TypedSelect(String caption) {
        setCaption(caption);
    }

    /**
     * Note, that with this constructor, you cannot override the select type.
     */
    public TypedSelect(String caption, Collection<T> listAllStyles) {
        this(caption);
        setOptions(listAllStyles);
    }
    
    public TypedSelect<T> withCaption(String caption) {
        setCaption(caption);
        return this;
    }
    
    public TypedSelect<T> withSelectType(Class<? extends AbstractSelect> selectType) {
        if (selectType == ListSelect.class) {
            select = new ListSelect() {
                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            };
        } else if (selectType == OptionGroup.class) {
            select = new OptionGroup() {
                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            };
        } else if (selectType == ComboBox.class) {
            select = new ComboBox() {
                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            };
        } else if (selectType == TwinColSelect.class) {
            select = new TwinColSelect() {
                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            };
        } else /*if (selectType == null || selectType == NativeSelect.class)*/ {
            select = new NativeSelect() {
                @SuppressWarnings("unchecked")
                @Override
                public String getItemCaption(Object itemId) {
                    return TypedSelect.this.getCaption((T) itemId);
                }
            };
        }
        return this;
    }
    
    protected final AbstractSelect getSelect() {
        if (select == null) {
            withSelectType(null);
            if (bic != null) {
                select.setContainerDataSource(bic);
            }
        }
        return select;
    }
    
    protected String getCaption(T option) {
        if (captionGenerator != null) {
            return captionGenerator.getCaption(option);
        }
        return option.toString();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public T getValue() {
        return (T) getSelect().getValue();
    }
    
    @Override
    public void focus() {
        getSelect().focus();
    }
    
    public final TypedSelect<T> setOptions(T... values) {
        return setOptions(Arrays.asList(values));
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Class<T> getType() {
        
        if (fieldType == null) {
            try {
                fieldType = (Class<T>) ((Container.Sortable) select
                        .getContainerDataSource()).firstItemId().getClass();
            } catch (Exception e) {
            }
        }
        return fieldType;
    }
    
    public TypedSelect setType(Class<T> type) {
        this.fieldType = type;
        return this;
    }
    
    @Override
    public boolean isInvalidCommitted() {
        return getSelect().isInvalidCommitted();
    }
    
    @Override
    public void setInvalidCommitted(boolean isCommitted) {
        getSelect().setInvalidCommitted(isCommitted);
    }
    
    @Override
    public void commit() throws SourceException, InvalidValueException {
        getSelect().commit();
    }
    
    @Override
    public void discard() throws SourceException {
        getSelect().discard();
    }
    
    @Override
    public void setBuffered(boolean buffered) {
        getSelect().setBuffered(buffered);
    }
    
    @Override
    public boolean isBuffered() {
        return getSelect().isBuffered();
    }
    
    @Override
    public boolean isModified() {
        return getSelect().isModified();
    }
    
    @Override
    public void addValidator(Validator validator) {
        getSelect().addValidator(validator);
    }
    
    @Override
    public void removeValidator(Validator validator) {
        getSelect().removeValidator(validator);
    }
    
    @Override
    public void removeAllValidators() {
        getSelect().removeAllValidators();
    }
    
    @Override
    public Collection<Validator> getValidators() {
        return getSelect().getValidators();
    }
    
    @Override
    public boolean isValid() {
        return getSelect().isValid();
    }
    
    @Override
    public void validate() throws InvalidValueException {
        getSelect().validate();
    }
    
    @Override
    public boolean isInvalidAllowed() {
        return getSelect().isInvalidAllowed();
    }
    
    @Override
    public void setInvalidAllowed(boolean invalidValueAllowed)
            throws UnsupportedOperationException {
        getSelect().setInvalidAllowed(invalidValueAllowed);
    }
    
    @Override
    public void setValue(T newValue) throws ReadOnlyException {
        getSelect().setValue(newValue);
    }
    
    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        getSelect().addValueChangeListener(listener);
    }
    
    @Override
    public void addListener(ValueChangeListener listener) {
        getSelect().addValueChangeListener(listener);
    }
    
    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        getSelect().removeValueChangeListener(listener);
    }
    
    @Override
    public void removeListener(ValueChangeListener listener) {
        getSelect().removeValueChangeListener(listener);
    }
    
    @Override
    public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
        getSelect().valueChange(event);
    }
    
    @Override
    public void setPropertyDataSource(Property newDataSource) {
        getSelect().setPropertyDataSource(newDataSource);
    }
    
    @Override
    public Property getPropertyDataSource() {
        return getSelect().getPropertyDataSource();
    }
    
    @Override
    public int getTabIndex() {
        return getSelect().getTabIndex();
    }
    
    @Override
    public void setTabIndex(int tabIndex) {
        getSelect().setTabIndex(tabIndex);
    }
    
    @Override
    public boolean isRequired() {
        return getSelect().isRequired();
    }
    
    @Override
    public void setRequired(boolean required) {
        getSelect().setRequired(required);
    }
    
    @Override
    public void setRequiredError(String requiredMessage) {
        getSelect().setRequiredError(requiredMessage);
    }
    
    @Override
    public String getRequiredError() {
        return getSelect().getRequiredError();
    }
    
    public CaptionGenerator<T> getCaptionGenerator() {
        return captionGenerator;
    }
    
    public TypedSelect<T> setCaptionGenerator(CaptionGenerator<T> captionGenerator) {
        this.captionGenerator = captionGenerator;
        return this;
    }
    
    public final TypedSelect<T> setOptions(Collection<T> options) {
        if (bic != null) {
            bic.setCollection(options);
        } else {
            bic = new ListContainer<T>(options);
            getSelect().setContainerDataSource(bic);
        }
        return this;
    }

    public TypedSelect<T> setBeans(Collection<T> options) {
        return setOptions(options);
    }
    
    @Override
    public void attach() {
        if (getCompositionRoot() == null) {
            setCompositionRoot(getSelect());
            if (bic != null && getSelect().getContainerDataSource() != bic) {
                getSelect().setContainerDataSource(bic);
            }
        }
        super.attach();
    }
    
}
