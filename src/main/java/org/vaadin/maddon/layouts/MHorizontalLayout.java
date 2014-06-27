package org.vaadin.maddon.layouts;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import java.util.Collection;

public class MHorizontalLayout extends HorizontalLayout {

    public MHorizontalLayout() {
        super.setSpacing(true);
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
    
    public MHorizontalLayout withMargin(MarginInfo marginInfo) {
        setMargin(marginInfo);
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

    /**
     * Expands selected components. Also sets the only sane width for expanded
     * components (100%).
     *
     * @param componentsToExpand
     * @return 
     */
    public MHorizontalLayout expand(Component... componentsToExpand) {
        for (Component component : componentsToExpand) {
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

    public MHorizontalLayout withAlign(Component component, Alignment alignment) {
        setComponentAlignment(component, alignment);
        return this;
    }

}
