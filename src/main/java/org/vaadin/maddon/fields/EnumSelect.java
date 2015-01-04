package org.vaadin.maddon.fields;

import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.ui.NativeSelect;

public class EnumSelect extends NativeSelect {

    public EnumSelect() {
    }

    public EnumSelect(String caption) {
        super(caption);
    }

    public EnumSelect withFullWidth() {
        setWidth("100%");
        return this;
    }

    public EnumSelect withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }

    public EnumSelect withValidator(Validator validator) {
        setImmediate(true);
        addValidator(validator);
        return this;
    }

    public EnumSelect withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public EnumSelect withWidth(String width) {
        setWidth(width);
        return this;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        if (newDataSource != null) {
            removeAllItems();
            Class<?> type = newDataSource.getType();
            List<?> asList = Arrays.asList(type.getEnumConstants());
            for (Object object : asList) {
                addItem(object);
            }
        }
        super.setPropertyDataSource(newDataSource);
    }

}
