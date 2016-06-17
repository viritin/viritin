package org.vaadin.viritin.layouts;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import org.vaadin.viritin.MSize;

import java.util.Collection;

public class MHorizontalLayout extends HorizontalLayout {

    public MHorizontalLayout() {
        super.setSpacing(true);
    }

    public MHorizontalLayout(Component... components) {
        this();
        addComponents(components);
    }

    public MHorizontalLayout with(Component... components) {
        addComponents(components);
        return this;
    }

    public MHorizontalLayout withSpacing(boolean spacing) {
        setSpacing(spacing);
        return this;
    }

    public MHorizontalLayout withMargin(boolean marging) {
        setMargin(marging);
        return this;
    }

    public MHorizontalLayout withMargin(MarginInfo marginInfo) {
        setMargin(marginInfo);
        return this;
    }

    public MHorizontalLayout withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MHorizontalLayout withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MHorizontalLayout withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MHorizontalLayout withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MHorizontalLayout withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MHorizontalLayout withHeight(float height, Unit unit) {
        setHeight(height, unit);
        return this;
    }

    public MHorizontalLayout withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MHorizontalLayout withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MHorizontalLayout alignAll(Alignment alignment) {
        for (Component component : this) {
            setComponentAlignment(component, alignment);
        }
        return this;
    }

    /**
     * Expands selected components. Also sets the only sane width for expanded
     * components (100%).
     *
     * @param componentsToExpand the components that should be expanded
     * @return the object itself for further configuration
     */
    public MHorizontalLayout expand(Component... componentsToExpand) {
        if (getWidth() < 0) {
            // Make full height if no other size is set
            withFullWidth();
        }

        for (Component component : componentsToExpand) {
            if (component.getParent() != this) {
                addComponent(component);
            }
            setExpandRatio(component, 1);
            component.setWidth(100, Unit.PERCENTAGE);
        }
        return this;
    }

    public MHorizontalLayout add(Component... component) {
        return with(component);
    }

    public MHorizontalLayout add(Collection<Component> component) {
        return with(component.toArray(new Component[component.size()]));
    }

    public MHorizontalLayout add(Component component, Alignment alignment) {
        return add(component).withAlign(component, alignment);
    }

    public MHorizontalLayout add(Component component, float ratio) {
        return add(component).withExpand(component, ratio);
    }

    public MHorizontalLayout add(Component component, Alignment alignment, float ratio) {
        return add(component).withAlign(component, alignment).withExpand(component, ratio);
    }

    public MHorizontalLayout withAlign(Component component, Alignment alignment) {
        setComponentAlignment(component, alignment);
        return this;
    }

    public MHorizontalLayout withExpand(Component component, float ratio) {
        setExpandRatio(component, ratio);
        return this;
    }

    public MHorizontalLayout withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MHorizontalLayout withCaption(String caption, boolean captionAsHtml) {
        setCaption(caption);
        setCaptionAsHtml(captionAsHtml);
        return this;
    }

    public MHorizontalLayout withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    /**
     * Adds "spacer" to layout that expands to consume remaining space. If
     * multiple spacers are added they share equally sized slot. Also tries to
     * configure layout for proper settings needed for this kind of usage.
     *
     * @return the layout with space added
     */
    public MHorizontalLayout space() {
        return expand(new Label());
    }


    public MHorizontalLayout withEnabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    public MHorizontalLayout withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public MHorizontalLayout withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    public MHorizontalLayout withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MHorizontalLayout withHeightUndefined() {
        setHeightUndefined();
        return this;
    }

    public MHorizontalLayout withResponsive(boolean responsive) {
        setResponsive(responsive);
        return this;
    }

    public MHorizontalLayout withDefaultComponentAlignment(Alignment defaultAlignment) {
        setDefaultComponentAlignment(defaultAlignment);
        return this;
    }

    public MHorizontalLayout withId(String id) {
        setId(id);
        return this;
    }

    public MHorizontalLayout withDescription(String description) {
        setDescription(description);
        return this;
    }

}
