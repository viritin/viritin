package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MultiSelectTable;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Address;
import org.vaadin.viritin.testdomain.Group;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;
import org.vaadin.viritin.util.BrowserCookie;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class BrowserCookieTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        final TextField key = new TextField("Cookie key");
        final TextField value = new TextField("Cookie Value");
        
        Button set = new Button("Set value", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                BrowserCookie.setCookie(key.getValue(), value.getValue());
            }
        });
        
        Button get = new Button("Detect value", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                BrowserCookie.detectCookieValue(key.getValue(), new BrowserCookie.Callback() {

                    @Override
                    public void onValueDetected(String value) {
                        Notification.show("Value:" + value, Notification.Type.WARNING_MESSAGE);
                    }
                });
            }
        });
        
        return new MVerticalLayout(key, value, set, get);
    }

}
