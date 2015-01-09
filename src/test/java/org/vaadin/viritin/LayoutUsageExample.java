package org.vaadin.viritin;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import junit.framework.Assert;
import org.junit.Test;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

// JUnit tests here
public class LayoutUsageExample {

    Component a, b, c, d;

    Panel mainContent = new Panel("Content");
    Tree menu = new Tree("Menu");

    public void layoutCodeExample() {

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        verticalLayout.setHeight("100%");
        HorizontalLayout horizontalLayout = new HorizontalLayout(c, d);
        horizontalLayout.setWidth("100%");
        horizontalLayout.setMargin(false);
        horizontalLayout.setSpacing(true);
        verticalLayout.addComponents(a, b, horizontalLayout);
        setContent(verticalLayout);

        // Is same as 
        setContent(new MVerticalLayout().withFullHeight().with(a, b,
                new MHorizontalLayout(c, d).withFullWidth().withMargin(false)));

        // Or ... 
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setMargin(true);
        wrapper.setSpacing(true);
        wrapper.setHeight("100%");
        HorizontalLayout toolbar = new HorizontalLayout(c, d);
        toolbar.setWidth("100%");
        toolbar.setMargin(false);
        toolbar.setSpacing(true);
        HorizontalLayout wrapper2 = new HorizontalLayout();
        wrapper2.addComponent(menu);
        wrapper2.addComponent(mainContent);
        wrapper2.setSizeFull();
        mainContent.setSizeFull();
        wrapper2.setExpandRatio(mainContent, 1);
        wrapper.addComponents(toolbar, wrapper2);
        wrapper.setExpandRatio(wrapper2, 1);
        setContent(wrapper);

        // Is same as:
        setContent(
                new MVerticalLayout(new MHorizontalLayout(c, d).withFullWidth())
                .expand(
                        new MHorizontalLayout(menu)
                        .expand(mainContent)
                )
        );
        // the expand call takes care of adding component and setting sane 
        // values for layout and the added component

    }

    private void setContent(VerticalLayout mVerticalLayout) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
