package org.vaadin.viritin.layouts;

import org.vaadin.viritin.MSize;
import org.vaadin.viritin.fluency.ui.FluentCssLayout;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;

public class MCssLayout extends CssLayout implements FluentCssLayout<MCssLayout> {

    private static final long serialVersionUID = 3994874408852708021L;

    public MCssLayout() {
    }

    public MCssLayout(Component... children) {
        super(children);
    }

    public MCssLayout add(Component... component) {
        addComponents(component);
        return this;
    }

    public MCssLayout withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MCssLayout withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MCssLayout withHeightUndefined() {
        setHeightUndefined();
        return this;
    }


}
