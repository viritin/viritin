package org.vaadin.viritin.layouts;

import java.util.Collection;

import org.vaadin.viritin.fluency.ui.FluentHorizontalLayout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class MHorizontalLayout extends HorizontalLayout
        implements FluentHorizontalLayout<MHorizontalLayout> {

    private static final long serialVersionUID = 524957578263653250L;

    public MHorizontalLayout() {
    }

    public MHorizontalLayout(Component... components) {
        this();
        addComponents(components);
    }

    public MHorizontalLayout with(Component... components) {
        addComponents(components);
        return this;
    }

    public MHorizontalLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

    /**
     * Expands selected components. Also sets the only sane width for expanded
     * components (100%).
     *
     * @param componentsToExpand the components that should be expanded
     * @return the object itself for further configuration
     */
    public MHorizontalLayout expand(Component... componentsToExpand) {
        if (getWidth() < 0) {
            // Make full height if no other size is set
            withFullWidth();
        }

        for (Component component : componentsToExpand) {
            if (component.getParent() != this) {
                addComponent(component);
            }
            setExpandRatio(component, 1);
            component.setWidth(100, Unit.PERCENTAGE);
        }
        return this;
    }

    public MHorizontalLayout add(Component... component) {
        return with(component);
    }

    public MHorizontalLayout add(Collection<Component> component) {
        return with(component.toArray(new Component[component.size()]));
    }

    public MHorizontalLayout add(Component component, Alignment alignment) {
        return add(component).withAlign(component, alignment);
    }

    public MHorizontalLayout add(Component component, float ratio) {
        return add(component).withExpand(component, ratio);
    }

    public MHorizontalLayout add(Component component, Alignment alignment, float ratio) {
        return add(component).withAlign(component, alignment).withExpand(component, ratio);
    }

    public MHorizontalLayout withAlign(Component component, Alignment alignment) {
        setComponentAlignment(component, alignment);
        return this;
    }

    public MHorizontalLayout withExpand(Component component, float ratio) {
        setExpandRatio(component, ratio);
        return this;
    }

    /**
     * Adds "spacer" to layout that expands to consume remaining space. If
     * multiple spacers are added they share equally sized slot. Also tries to
     * configure layout for proper settings needed for this kind of usage.
     *
     * @return the layout with space added
     */
    public MHorizontalLayout space() {
        return expand(new Label());
    }
}
