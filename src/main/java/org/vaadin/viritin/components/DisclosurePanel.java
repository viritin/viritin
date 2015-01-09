package org.vaadin.viritin.components;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 */
public class DisclosurePanel extends VerticalLayout {

    private FontIcon closedIcon = FontAwesome.PLUS_CIRCLE;
    private FontIcon openIcon = FontAwesome.MINUS_CIRCLE;

    private MButton toggle = new MButton(closedIcon);
    private MVerticalLayout contentWrapper = new MVerticalLayout();

    public DisclosurePanel() {
        toggle.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        contentWrapper.setVisible(false);
        addComponents(toggle, contentWrapper);
        toggle.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setOpen(!isOpen());
            }
        });
    }

    public DisclosurePanel(String caption, Component... content) {
        this();
        setCaption(caption);
        contentWrapper.add(content);
    }

    public boolean isOpen() {
        return toggle.getIcon() == openIcon;
    }

    public DisclosurePanel setOpen(boolean open) {
        contentWrapper.setVisible(open);
        toggle.setIcon(open ? getOpenIcon() : getClosedIcon());
        return this;
    }

    public DisclosurePanel setContent(Component... content) {
        this.contentWrapper.removeAllComponents();
        this.contentWrapper.add(content);
        return this;
    }

    @Override
    public void setCaption(String caption) {
        toggle.setCaption(caption);
    }

    public MVerticalLayout getContentWrapper() {
        return contentWrapper;
    }

    public FontIcon getClosedIcon() {
        return closedIcon;
    }

    public void setClosedIcon(FontIcon closedIcon) {
        this.closedIcon = closedIcon;
    }

    public FontIcon getOpenIcon() {
        return openIcon;
    }

    public void setOpenIcon(FontIcon openIcon) {
        this.openIcon = openIcon;
    }

}
