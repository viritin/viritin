package org.vaadin.viritin.fields;


import java.util.Collection;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.NativeSelect;

public class EnumSelect<T> extends TypedSelect<T> {

    private static final long serialVersionUID = -4871997560701783780L;

    public EnumSelect() {
        withSelectType(NativeSelect.class);
        setWidthUndefined();
    }

    public EnumSelect(String caption) {
        super(caption);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        if (newDataSource != null) {
            Class<T> type = newDataSource.getType();
            setOptions(type.getEnumConstants());
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public EnumSelect<T> setBeans(Collection<T> options) {
        return (EnumSelect<T>) super.setBeans(options);
    }

    @Override
    public EnumSelect<T> setFieldType(Class<T> type) {
        return (EnumSelect<T>) super.setFieldType(type);
    }

    @Override
    public EnumSelect<T> withWidth(String width) {
        return (EnumSelect<T>) super.withWidth(width);
    }

    @Override
    public EnumSelect<T> withWidth(float width, Unit unit) {
        return (EnumSelect<T>) super.withWidth(width, unit);
    }

    @Override
    public EnumSelect<T> withValidator(Validator validator) {
        return (EnumSelect<T>) super.withValidator(validator);
    }

    @Override
    public TypedSelect<T> withReadOnly(boolean readOnly) {
        return super.withReadOnly(readOnly);
    }

    @Override
    public EnumSelect<T> withFullWidth() {
        return (EnumSelect<T>) super.withFullWidth();
    }

    @Override
    public EnumSelect<T> addMValueChangeListener(MValueChangeListener<T> listener) {
        return (EnumSelect<T>) super.addMValueChangeListener(listener);
    }

    @Override
    public EnumSelect<T> withSelectType(Class<? extends AbstractSelect> selectType) {
        return (EnumSelect<T>) super.withSelectType(selectType);
    }

    @Override
    public EnumSelect<T> withCaption(String caption) {
        return (EnumSelect<T>) super.withCaption(caption);
    }

    public EnumSelect<T> withNullSelection(boolean allowNullSelection) {
        getSelect().setNullSelectionAllowed(allowNullSelection);
        return this;
    }
    
}
