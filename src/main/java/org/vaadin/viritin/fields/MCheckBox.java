package org.vaadin.viritin.fields;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.Resource;
import com.vaadin.ui.CheckBox;

public class MCheckBox extends CheckBox {

    private static final long serialVersionUID = 2889392345462991407L;

    public MCheckBox() {
    }

    public MCheckBox(String caption) {
        super(caption);
    }

    public MCheckBox(String caption, boolean initialState) {
        super(caption, initialState);
    }

    public MCheckBox(String caption, Property<?> dataSource) {
        super(caption, dataSource);
    }

    public MCheckBox withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MCheckBox withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MCheckBox withValue(Boolean value) {
        setValue(value);
        return this;
    }

    public boolean isChecked() {
        return getValue() != null ? getValue() : false;
    }

    public MCheckBox withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MCheckBox withRequired(boolean required) {
        setRequired(required);
        return this;
    }

    public MCheckBox withRequiredError(String requiredError) {
        setRequiredError(requiredError);
        return this;
    }

    public MCheckBox withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public MCheckBox withValueChangeListener(Property.ValueChangeListener listener) {
        addValueChangeListener(listener);
        return this;
    }

    public MCheckBox withBlurListener(FieldEvents.BlurListener listener) {
        addBlurListener(listener);
        return this;
    }

    public MCheckBox withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MCheckBox withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MCheckBox withWidth(String width) {
        setWidth(width);
        return this;
    }
}
