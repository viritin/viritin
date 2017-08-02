package org.vaadin.viritin.layouts;

import org.vaadin.viritin.fluency.ui.FluentFormLayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

public class MFormLayout extends FormLayout
        implements FluentFormLayout<MFormLayout> {

    private static final long serialVersionUID = -4097293516833876208L;

    public MFormLayout() {
    }

    public MFormLayout(Component... children) {
        super(children);
    }

    public MFormLayout withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MFormLayout withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MFormLayout withHeightUndefined() {
        setHeightUndefined();
        return this;
    }

    public MFormLayout with(Component... components) {
        addComponents(components);
        return this;
    }
}
