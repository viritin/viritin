package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.time.LocalDateTime;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.BrowserCookie;

/**
 *
 * @author Matti Tahvonen
 */
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

        Button setWithExpiration = new Button("Set value with Expiration", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                BrowserCookie.setCookie(key.getValue(), value.getValue(), LocalDateTime.now().plusMinutes(1));
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
        
        return new MVerticalLayout(key, value, set, setWithExpiration, get);
    }

}
