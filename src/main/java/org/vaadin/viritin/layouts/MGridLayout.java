package org.vaadin.viritin.layouts;

import java.util.Collection;

import org.vaadin.viritin.MSize;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;

public class MGridLayout extends FGridLayout {

    public MGridLayout() {
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

    public MGridLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MGridLayout withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MGridLayout withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
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

    public MGridLayout withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }
}
