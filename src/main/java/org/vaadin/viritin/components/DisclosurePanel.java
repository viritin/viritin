package org.vaadin.viritin.components;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 */
public class DisclosurePanel extends VerticalLayout {

    private static final long serialVersionUID = 6509419456771505782L;

    private FontIcon closedIcon = FontAwesome.PLUS_CIRCLE;
    private FontIcon openIcon = FontAwesome.MINUS_CIRCLE;

    private final MButton toggle = new MButton(closedIcon);
    private final MVerticalLayout contentWrapper = new MVerticalLayout();

    public DisclosurePanel() {
        setMargin(false);
        setSpacing(false);
        toggle.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        contentWrapper.setVisible(false);
        addComponents(toggle, contentWrapper);
        toggle.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -3471451934465654395L;

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
        return contentWrapper.isVisible();
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

    public DisclosurePanel setClosedIcon(FontIcon closedIcon) {
        this.closedIcon = closedIcon;
        return setOpen(isOpen());
    }

    public FontIcon getOpenIcon() {
        return openIcon;
    }

    public DisclosurePanel setOpenIcon(FontIcon openIcon) {
        this.openIcon = openIcon;
        return setOpen(isOpen());
    }

}
