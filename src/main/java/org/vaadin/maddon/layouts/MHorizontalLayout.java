package org.vaadin.maddon.layouts;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class MHorizontalLayout extends HorizontalLayout {

    public MHorizontalLayout() {
        super.setSpacing(true);
        super.setMargin(true);
    }

    public MHorizontalLayout(Component... components) {
        this();
        addComponents(components);
    }

    public MHorizontalLayout with(Component... components) {
        addComponents(components);
        return this;
    }

    public MHorizontalLayout withSpacing(boolean spacing) {
        setSpacing(spacing);
        return this;
    }

    public MHorizontalLayout withMargin(boolean marging) {
        setMargin(marging);
        return this;
    }

    public MHorizontalLayout withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MHorizontalLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MHorizontalLayout withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MHorizontalLayout withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MHorizontalLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

}
