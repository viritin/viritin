package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.MButton.MClickListener;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MButtonClickListeners extends AbstractTest {

    private static final long serialVersionUID = -8785308275350618769L;

    @Override
    public Component getTestComponent() {
        
        MButton b = new MButton("Say hola");
        
        // And the same without lambdas as the project is 1.6 compatible
        b.addClickListener(new MClickListener() {

            @Override
            public void onClick() {
                sayHola();
            }
        });
        b.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;
            
            @Override
            public void buttonClick(ClickEvent event) {
                sayHolaOldSchool(event);
            }
        });
        
        return new MVerticalLayout(b);
    }
    
    public void sayHola() {
        Notification.show("Hola!");
    }
    
    public void sayHolaOldSchool(ClickEvent e) {
        Notification.show("Hola oldschool fellow!", Notification.Type.TRAY_NOTIFICATION);
    }

}
