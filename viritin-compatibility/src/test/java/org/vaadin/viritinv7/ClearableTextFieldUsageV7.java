package org.vaadin.viritinv7;

import org.vaadin.viritin.it.*;
import com.vaadin.server.UserError;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritinv7.fields.ClearableTextField;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Niki
 */
public class ClearableTextFieldUsageV7 extends AbstractTest {

    @Override
    public Component getTestComponent() {

        ClearableTextField ctf = new ClearableTextField();
        ctf.setCaption("CTF caption");
        ctf.setInputPrompt("Enter text");
        ctf.setValue("Some Text");
        
        ctf.setImmediate(true);

        ctf.addValueChangeListener(e -> {
            Notification.show("Value: " + ctf.getValue());
        });

        MButton b = new MButton("Toggle required", e -> {
            ctf.setRequired(!ctf.isRequired());
        });
        MButton b2 = new MButton("Toggle error", e -> {
            if (ctf.getComponentError() == null) {
                ctf.setComponentError(new UserError("Must be filled"));
            } else {
                ctf.setComponentError(null);
            }
        });

        MButton b3 = new MButton("Toggle readonly", e -> {
            ctf.setReadOnly(!ctf.isReadOnly());
        });

        MButton b4 = new MButton("Toggle enabled", e -> {
            ctf.setEnabled(!ctf.isEnabled());
        });

        return new MPanel()
                .withCaption("ClearableTextField")
                .withDescription("Click the X to clear…")
                .withFullWidth()
                .withFullHeight()
                .withContent(new MVerticalLayout(ctf, b, b2, b3, b4));
    }
}
