package org.vaadin.maddon.fields;


import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.NativeSelect;
import java.util.Collection;

public class EnumSelect extends TypedSelect {

    public EnumSelect() {
        setFieldType(NativeSelect.class);
    }

    public EnumSelect(String caption) {
        super(caption);
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        if (newDataSource != null) {
            Class<?> type = newDataSource.getType();
            setOptions(type.getEnumConstants());
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    public EnumSelect setBeans(Collection options) {
        return (EnumSelect) super.setBeans(options);
    }

    @Override
    public EnumSelect setCaptionGenerator(CaptionGenerator captionGenerator) {
        return (EnumSelect) super.setCaptionGenerator(captionGenerator);
    }

    @Override
    public EnumSelect setFieldType(Class type) {
        return (EnumSelect) super.setFieldType(type);
    }

    @Override
    public EnumSelect withWidth(String width) {
        return (EnumSelect) super.withWidth(width);
    }

    @Override
    public EnumSelect withWidth(float width, Unit unit) {
        return (EnumSelect) super.withWidth(width, unit);
    }

    @Override
    public EnumSelect withValidator(Validator validator) {
        return (EnumSelect) super.withValidator(validator);
    }

    @Override
    public TypedSelect withReadOnly(boolean readOnly) {
        return super.withReadOnly(readOnly);
    }

    @Override
    public EnumSelect withFullWidth() {
        return (EnumSelect) super.withFullWidth();
    }

    @Override
    public EnumSelect addMValueChangeListener(MValueChangeListener listener) {
        return (EnumSelect) super.addMValueChangeListener(listener);
    }

    @Override
    public EnumSelect withSelectType(Class selectType) {
        return (EnumSelect) super.withSelectType(selectType);
    }

    @Override
    public EnumSelect withCaption(String caption) {
        return (EnumSelect) super.withCaption(caption);
    }
    
}
