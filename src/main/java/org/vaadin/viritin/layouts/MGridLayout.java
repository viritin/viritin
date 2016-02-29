package org.vaadin.viritin.layouts;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import org.vaadin.viritin.MSize;

import java.util.Collection;

public class MGridLayout extends GridLayout {

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

    public MGridLayout withSpacing(boolean spacing) {
        setSpacing(spacing);
        return this;
    }

    public MGridLayout withMargin(boolean marging) {
        setMargin(marging);
        return this;
    }

    public MGridLayout withMargin(MarginInfo marginInfo) {
        setMargin(marginInfo);
        return this;
    }

    public MGridLayout withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MGridLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MGridLayout withHeight(String height) {
        setHeight(height);
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

    public MGridLayout withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MGridLayout withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MGridLayout withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }
}
