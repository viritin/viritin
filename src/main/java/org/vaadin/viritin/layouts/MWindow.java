package org.vaadin.viritin.layouts;

import org.vaadin.viritin.MSize;
import org.vaadin.viritin.fluency.ui.IWindow;

import com.vaadin.event.Action;
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

    @Override
    public MWindow withWidthFull() {
        return withWidth("100%");
    }

    @Override
    public MWindow withHeightFull() {
        return withHeight("100%");
    }

    @Override
    public <T extends Action & com.vaadin.event.Action.Listener> MWindow withAction(
            T action) {
        addAction(action);
        return this;
    }
}
