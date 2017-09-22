package org.vaadin.viritin.layouts;

import java.util.Collection;

import org.vaadin.viritin.fluency.ui.FluentVerticalLayout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class MVerticalLayout extends VerticalLayout
        implements FluentVerticalLayout<MVerticalLayout> {

    private static final long serialVersionUID = -1806208156595232451L;

    public MVerticalLayout() {
    }

    public MVerticalLayout(Component... components) {
        this();
        addComponents(components);
    }

    public MVerticalLayout with(Component... components) {
        addComponents(components);
        return this;
    }

    public MVerticalLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

    /**
     * Expands selected components. Also adds to layout and sets the only sane
     * height for expanded components (100%) if needed.
     *
     * @param componentsToExpand components that should be expanded
     * @return the object itself for further configuration
     */
    public MVerticalLayout expand(Component... componentsToExpand) {
        if (getHeight() < 0) {
            // Make full height if no other size is set
            withFullHeight();
        }
        for (Component component : componentsToExpand) {
            if (component.getParent() != this) {
                addComponent(component);
            }
            setExpandRatio(component, 1);
            component.setHeight(100, Unit.PERCENTAGE);
        }
        return this;
    }

    public MVerticalLayout add(Component... component) {
        return with(component);
    }

    public MVerticalLayout add(Collection<Component> component) {
        return with(component.toArray(new Component[component.size()]));
    }

    public MVerticalLayout add(Component component, Alignment alignment) {
        return add(component).withAlign(component, alignment);
    }

    public MVerticalLayout add(Component component, float ratio) {
        return add(component).withExpand(component, ratio);
    }

    public MVerticalLayout add(Component component, Alignment alignment, float ratio) {
        return add(component).withAlign(component, alignment).withExpand(component, ratio);
    }

    public MVerticalLayout withAlign(Component component, Alignment alignment) {
        setComponentAlignment(component, alignment);
        return this;
    }

    public MVerticalLayout withExpand(Component component, float ratio) {
        setExpandRatio(component, ratio);
        return this;
    }

    public MVerticalLayout withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MVerticalLayout withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MVerticalLayout withHeightUndefined() {
        setHeightUndefined();
        return this;
    }

}
