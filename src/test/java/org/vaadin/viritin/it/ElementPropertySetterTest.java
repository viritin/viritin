package org.vaadin.viritin.it;

import com.vaadin.annotations.StyleSheet;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.HtmlElementPropertySetter;

/**
 * Tests for raw usage of HtmlElementPropertySetter. Note, using
 * HtmlElementPropertySetter directly is not suggested. Most often you should
 * hide these browser quirks behind custom components. On TODO to Viritin at 
 * least IntegerField, DoubleField, NumberRange
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
@StyleSheet("valo_shadow_dom_enhancements.css")
public class ElementPropertySetterTest extends AbstractTest {

    private final ValueChangeListener vcl = new ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            Notification.show(event.getProperty().getValue().toString());
        }
    };

    @Override
    public Component getTestComponent() {

        final MVerticalLayout mVerticalLayout = new MVerticalLayout();

        Button b = new Button("Do stuff with things");

        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                TextField rawTextField = new TextField();
                rawTextField.addValueChangeListener(vcl);

                HtmlElementPropertySetter s1 = new HtmlElementPropertySetter(
                        rawTextField);
                s1.setProperty("type", "number");
                s1.setProperty("min", "10");
                s1.setProperty("max", "100");
                // prevent all but numbers with a simple js
                s1.setJavaScriptEventHandler("keypress",
                        "function(e) {var c = viritin.getChar(e); return c==null || /^[\\d\\n\\t\\r]+$/.test(c);}");
                mVerticalLayout.add(rawTextField);

                HtmlElementPropertySetter s2 = new HtmlElementPropertySetter(
                        mVerticalLayout);
                // sets with xpath, same could be also done via s1 as 
                // using document global selector (// vs .//)
                s2.setProperty("//input", "step", "10");
                s2.setProperty("//input", "required", "true");

                TextField rawTextField2 = new TextField();
                TextField rawTextField3 = new TextField();
                rawTextField2.addValueChangeListener(vcl);
                rawTextField3.addValueChangeListener(vcl);
                final MVerticalLayout dates = new MVerticalLayout(rawTextField2,
                        rawTextField3).withCaption("dates");

                HtmlElementPropertySetter s3 = new HtmlElementPropertySetter(
                        dates);
                // set all inputs inside dates layout to be of type date
                s3.setProperty(".//input", "type", "date");

                mVerticalLayout.add(dates);

            }
        });

        mVerticalLayout.add(b);

        return mVerticalLayout;
    }

}
