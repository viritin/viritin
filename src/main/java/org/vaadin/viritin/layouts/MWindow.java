package org.vaadin.viritin.layouts;

import org.vaadin.viritin.MSize;

import com.vaadin.ui.Component;

public class MWindow extends FWindow {

    public MWindow() {
        super();
    }

    public MWindow(String caption) {
        super(caption);
    }

    public MWindow(String caption, Component content) {
        super(caption, content);
    }

    public MWindow withFullWidth() {
        return withWidth("100%");      
    }

    public MWindow withFullHeight() {
        return withHeight("100%");
    }

    public MWindow withCenter() {
        center();
        return this;
    }

    public MWindow withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MWindow withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }
}
