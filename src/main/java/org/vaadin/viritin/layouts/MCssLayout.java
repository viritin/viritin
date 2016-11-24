package org.vaadin.viritin.layouts;

import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import org.vaadin.viritin.MSize;

public class MCssLayout extends FCssLayout {

    public MCssLayout() {
    }

    public MCssLayout(Component... children) {
        super(children);
    }

    public MCssLayout withFullWidth() {
        setWidth("100%");
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

    public MCssLayout withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MCssLayout add(Component... component) {
        addComponents(component);
        return this;
    }

    public MCssLayout withCaption(String caption, boolean captionAsHtml) {
        setCaption(caption);
        setCaptionAsHtml(captionAsHtml);
        return this;
    }

    public MCssLayout withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public MCssLayout withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MCssLayout withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MCssLayout withHeightUndefined() {
        setHeightUndefined();
        return this;
    }

    public MCssLayout withResponsive(boolean responsive) {
        setResponsive(responsive);
        return this;
    }

    public MCssLayout withId(String id) {
        setId(id);
        return this;
    }

    public MCssLayout withDescription(String description) {
        setDescription(description);
        return this;
    }


}
