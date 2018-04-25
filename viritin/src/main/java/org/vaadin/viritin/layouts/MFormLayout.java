package org.vaadin.viritin.layouts;

import com.vaadin.ui.Alignment;
import org.vaadin.viritin.fluency.ui.FluentFormLayout;

import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;

import java.util.Collection;

public class MFormLayout extends FormLayout
        implements FluentFormLayout<MFormLayout> {

    private static final long serialVersionUID = -4097293516833876208L;

    public MFormLayout() {
    }

    public MFormLayout(Component... children) {
        super(children);
    }

    public MFormLayout with(Component... components) {
        addComponents(components);
        return this;
    }

    public MFormLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

    public MFormLayout add(Component... component) {
        return with(component);
    }

    public MFormLayout add(Collection<Component> component) {
        return with(component.toArray(new Component[component.size()]));
    }

    public MFormLayout add(Component component, Alignment alignment) {
        add(component);
        setComponentAlignment(component, alignment);
        return this;
    }

}
