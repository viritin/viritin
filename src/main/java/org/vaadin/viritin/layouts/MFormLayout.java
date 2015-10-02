package org.vaadin.viritin.layouts;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import org.vaadin.viritin.MSize;

public class MFormLayout extends FormLayout {

    public MFormLayout() {
    }

    public MFormLayout(Component... children) {
        super(children);
    }

    public MFormLayout withMargin(boolean marging) {
        setMargin(marging);
        return this;
    }

    public MFormLayout withMargin(MarginInfo marginInfo) {
        setMargin(marginInfo);
        return this;
    }

    public MFormLayout withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MFormLayout withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MFormLayout withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    public MFormLayout withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MFormLayout withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MFormLayout withStyleName(String styleName) {
        setStyleName(styleName);
        return this;
    }


    public MFormLayout withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

}
