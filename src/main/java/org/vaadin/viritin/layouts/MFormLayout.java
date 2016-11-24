package org.vaadin.viritin.layouts;

import org.vaadin.viritin.MSize;

import com.vaadin.ui.Component;

public class MFormLayout extends FFormLayout {

    public MFormLayout() {
    }

    public MFormLayout(Component... children) {
        super(children);
    }

    public MFormLayout withCaption(String caption, boolean captionAsHtml) {
        setCaption(caption);
        setCaptionAsHtml(captionAsHtml);
        return this;
    }

    public MFormLayout withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    public MFormLayout withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MFormLayout withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MFormLayout with(Component... components) {
        addComponents(components);
        return this;
    }
}
