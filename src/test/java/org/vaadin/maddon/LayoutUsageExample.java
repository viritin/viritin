package org.vaadin.maddon;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import junit.framework.Assert;
import org.junit.Test;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

// JUnit tests here
public class LayoutUsageExample {

    Component a, b, c, d;

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

    }

    private void setContent(VerticalLayout mVerticalLayout) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
