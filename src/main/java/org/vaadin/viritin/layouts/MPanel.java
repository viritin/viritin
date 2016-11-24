package org.vaadin.viritin.layouts;

import org.vaadin.viritin.MSize;

import com.vaadin.ui.Component;

/**
 * Fluent Panel
 */
public class MPanel extends FPanel{

    private static final long serialVersionUID = -7384406421724902868L;

    public MPanel() {
        super();
    }

    public MPanel(Component content) {
        super(content);
    }

    public MPanel(String caption) {
        super(caption);
    }

    public MPanel(String caption, Component content) {
        super(caption, content);
    }

    public MPanel withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MPanel withFullHeight() {
        setHeight("100%");
        return this;
    }
    public MPanel withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MPanel withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MPanel withEnabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    public MPanel withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public MPanel withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MPanel withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MPanel withHeightUndefined() {
        setHeightUndefined();
        return this;
    }

    public MPanel withResponsive(boolean responsive) {
        setResponsive(responsive);
        return this;
    }

    public MPanel withId(String id) {
        setId(id);
        return this;
    }

    public MPanel withDescription(String description) {
        setDescription(description);
        return this;
    }
}
