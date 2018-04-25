package org.vaadin.viritin.layouts;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import org.vaadin.viritin.fluency.ui.FluentFormLayout;

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
        return add(component).withAlign(component, alignment);
    }

    public MFormLayout add(Component component, float ratio) {
        return add(component).withExpand(component, ratio);
    }

    public MFormLayout add(Component component, Alignment alignment, float ratio) {
        return add(component).withAlign(component, alignment).withExpand(component, ratio);
    }

    public MFormLayout withAlign(Component component, Alignment alignment) {
        setComponentAlignment(component, alignment);
        return this;
    }

    public MFormLayout withExpand(Component component, float ratio) {
        setExpandRatio(component, ratio);
        return this;
    }

}
