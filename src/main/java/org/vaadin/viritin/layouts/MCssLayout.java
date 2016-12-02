package org.vaadin.viritin.layouts;

import org.vaadin.viritin.MSize;

import com.vaadin.ui.Component;

public class MCssLayout extends FCssLayout  {

    private static final long serialVersionUID = 3994874408852708021L;

    public MCssLayout() {
    }

    public MCssLayout(Component... children) {
        super(children);
    }

    @Override
    public MCssLayout withFullWidth() {
        return withWidth("100%");
    }

    @Override
    public MCssLayout withFullHeight() {
        return withHeight("100%");
    }

    public MCssLayout withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    @Override
    public MCssLayout withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
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
}
