package org.vaadin.viritin.layouts;

import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class MCssLayout extends CssLayout {

    public MCssLayout() {
    }

    public MCssLayout(Component... children) {
        super(children);
    }

    public MCssLayout withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MCssLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MCssLayout withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MCssLayout withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MCssLayout withStyleName(String styleName) {
        setStyleName(styleName);
        return this;
    }
}
