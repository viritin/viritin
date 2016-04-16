package org.vaadin.viritin.layouts;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;
import org.vaadin.viritin.MSize;

public class MWindow extends Window {

    public MWindow() {
        super();
    }

    public MWindow(String caption) {
        super(caption);
    }

    public MWindow(String caption, Component content) {
        super(caption, content);
    }

    public MWindow withContent(Component content) {
        setContent(content);
        return this;
    }

    public MWindow withModal(boolean modal) {
        setModal(modal);
        return this;
    }

    public MWindow withClosable(boolean closable) {
        setClosable(closable);
        return this;
    }

    public MWindow withResizable(boolean resizable) {
        setResizable(resizable);
        return this;
    }

    public MWindow withDraggable(boolean draggable) {
        setDraggable(draggable);
        return this;
    }

    public MWindow withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MWindow withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MWindow withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MWindow withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MWindow withHeight(float height, Unit unit) {
        setHeight(height, unit);
        return this;
    }

    public MWindow withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MWindow withCenter() {
        center();
        return this;
    }

    public MWindow withPosition(int x, int y) {
        setPosition(x,y);
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
