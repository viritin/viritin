package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.BrowserCookie;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class BrowserCookieTestIssue109 extends AbstractTest {

	public BrowserCookieTestIssue109() {
        getUI().setPollInterval(1000);
	}

	@Override
    public Component getTestComponent() {
        final TextField key = new TextField("Cookie key");
        final TextField value = new TextField("Cookie Value");

        Button set = new Button("Set value with timeout from another thread",
                new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(BrowserCookieTestIssue109.class.getName()).
                                    log(Level.SEVERE, null, ex);
                        }
                        access(new Runnable() {
                            @Override
                            public void run() {
                                BrowserCookie.setCookie(key.getValue(), value.
                                        getValue());
                                Notification.show("Cookie set!");
                            }
                        });
                    }
                }.start();
                
            }
        });

        Button get = new Button("Detect value", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                BrowserCookie.detectCookieValue(key.getValue(),
                        new BrowserCookie.Callback() {

                    @Override
                    public void onValueDetected(String value) {
                        Notification.show("Value:" + value,
                                Notification.Type.WARNING_MESSAGE);
                    }
                });
            }
        });

        return new MVerticalLayout(key, value, set, get);
    }

}
