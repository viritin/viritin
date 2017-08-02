package org.vaadin.viritin.fields;

import org.vaadin.viritin.fluency.ui.FluentAbstractField;
import org.vaadin.viritin.fluency.ui.FluentHasValue;

import com.vaadin.ui.CheckBox;

public class MCheckBox extends CheckBox
        implements FluentAbstractField<MCheckBox, Boolean>,
        FluentHasValue<MCheckBox, Boolean> {

    public MCheckBox() {
        super();
    }

    public MCheckBox(String caption, boolean initialState) {
        super(caption, initialState);
    }

    public MCheckBox(String caption) {
        super(caption);
    }
}
