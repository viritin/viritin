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

    @Override
    public Component getTestComponent() {
        
        MButton b = new MButton("Say hola");
//        b.addClickListener(this::sayHola);
//        b.addClickListener(this::sayHolaOldSchool);
        
        // And the same without lambdas as the project is 1.6 compatible
        b.addClickListener(new MClickListener() {

            @Override
            public void onClick() {
                sayHola();
            }
        });
        b.addClickListener(new Button.ClickListener() {
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
