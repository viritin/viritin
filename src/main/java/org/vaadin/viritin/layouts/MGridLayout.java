package org.vaadin.viritin.layouts;

import java.util.Collection;

import org.vaadin.viritin.fluency.ui.FluentGridLayout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

public class MGridLayout extends GridLayout
        implements FluentGridLayout<MGridLayout> {

    private static final long serialVersionUID = -4353934595461037075L;

    public MGridLayout() {
        super.setSpacing(true);
        super.setMargin(true);
    }

    public MGridLayout(int columns, int rows) {
        super(columns, rows);
        super.setSpacing(true);
        super.setMargin(true);
    }

    public MGridLayout(int columns, int rows, Component... children) {
        super(columns, rows, children);
        super.setSpacing(true);
        super.setMargin(true);
    }
    
    public MGridLayout(Component... components) {
        this();
        addComponents(components);
    }

    public MGridLayout with(Component... components) {
        addComponents(components);
        return this;
    }

    public MGridLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

    public MGridLayout add(Component... component) {
        return with(component);
    }

    public MGridLayout add(Collection<Component> component) {
        return with(component.toArray(new Component[component.size()]));
    }

    public MGridLayout withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MGridLayout withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MGridLayout withHeightUndefined() {
        setHeightUndefined();
        return this;
    }
}
