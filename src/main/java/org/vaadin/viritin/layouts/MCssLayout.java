package org.vaadin.viritin.layouts;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.MSize;

public class MCssLayout extends CssLayout {

    public MCssLayout() {
    }

    public MCssLayout(Component... children) {
        super(children);
    }

    public MCssLayout withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MCssLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MCssLayout withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MCssLayout withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MCssLayout withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MCssLayout withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MCssLayout withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }
}
