package org.vaadin.viritin.fields;

import com.vaadin.data.Property;

public class MCheckBox extends FCheckBox {

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

    public MCheckBox withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public boolean isChecked() {
        return getValue() != null ? getValue() : false;
    }

    public MCheckBox withFullWidth() {
        return withWidth("100%");      
    }
}
