package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.server.*;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Widgetset("com.vaadin.v7.Vaadin7WidgetSet")
public abstract class AbstractTest extends UI {

    protected VerticalLayout content;

    public AbstractTest() {
        content = new VerticalLayout();
        setContent(content);
    }

    protected void setup() {
        Component map = getTestComponent();
        setContentSize(content);
        content.addComponent(map);
        content.setExpandRatio(map, 1);
    }

    /**
     * Sets the size of the content. Override to change from
     * {@link Sizeable#setSizeFull()}
     * 
     * @param content the content component
     */
    public void setContentSize(Component content) {
        content.setSizeFull();
    }

    public abstract Component getTestComponent();

    @Override
    protected void init(VaadinRequest request) {
        setup();
        if (StringUtils.isNotBlank(getDescription())) {
            Notification.show(getDescription(),
                    Notification.Type.WARNING_MESSAGE);
        }
    }

}
