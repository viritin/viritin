package org.vaadin.maddon.layouts;

import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

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

    public MWindow withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MWindow withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MWindow withFullHeight() {
        setHeight("100%");
        return this;
    }

}
