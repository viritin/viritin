package org.vaadin.viritin.layouts;

import org.vaadin.viritin.MSize;
import org.vaadin.viritin.fluency.ui.IPanel;

import com.vaadin.event.Action;
import com.vaadin.ui.Component;

/**
 * Fluent Panel
 */
public class MPanel extends FPanel {

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

    @Override
    public <T extends Action & com.vaadin.event.Action.Listener> MPanel withAction(
            T action) {
       addAction(action);
       return this;
    }
}
