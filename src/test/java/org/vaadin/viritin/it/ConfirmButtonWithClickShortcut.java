package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * An example how to use DownloadButton
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class ConfirmButtonWithClickShortcut extends AbstractTest {

    private static final long serialVersionUID = 2638100034569162593L;

    @Override
    public Component getTestComponent() {

        ConfirmButton d = new ConfirmButton("Do things with escape key shortcut...", "Are you sure", e->{
            Notification.show("You did it!", Notification.Type.ERROR_MESSAGE);
        });

        d.setClickShortcut(KeyCode.ESCAPE);

        return new MVerticalLayout(d);

    }

}
