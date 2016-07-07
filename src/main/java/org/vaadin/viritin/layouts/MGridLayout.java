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

    public MGridLayout withWidth(float width, Unit unit) {
        setWidth(width, unit);
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

    public MGridLayout withHeight(float height, Unit unit) {
        setHeight(height, unit);
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

    public MGridLayout withCaption(String caption, boolean captionAsHtml) {
        setCaption(caption);
        setCaptionAsHtml(captionAsHtml);
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

    public MGridLayout withVisible(boolean visible) {
        setVisible(visible);
        return this;
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

    public MGridLayout withResponsive(boolean responsive) {
        setResponsive(responsive);
        return this;
    }

    public MGridLayout withDefaultComponentAlignment(Alignment defaultAlignment) {
        setDefaultComponentAlignment(defaultAlignment);
        return this;
    }

    public MGridLayout withId(String id) {
        setId(id);
        return this;
    }

    public MGridLayout withDescription(String description) {
        setDescription(description);
        return this;
    }
}
