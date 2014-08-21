package org.vaadin.maddon.layouts;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class MFormLayout extends FormLayout {

    public MFormLayout() {
    }

    public MFormLayout(Component... children) {
        super(children);
    }

    public MFormLayout withMargin(boolean marging) {
        setMargin(marging);
        return this;
    }

    public MFormLayout withMargin(MarginInfo marginInfo) {
        setMargin(marginInfo);
        return this;
    }

    public MFormLayout withCaption(String caption) {
        setCaption(caption);
        return this;
    }

}
