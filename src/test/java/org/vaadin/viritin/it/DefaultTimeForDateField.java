package org.vaadin.viritin.it;

import com.vaadin.v7.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Notification;
import java.util.Date;
import java.util.Map;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * An example how to extend 
 * @author Matti Tahvonen
 */
public class DefaultTimeForDateField extends AbstractTest {

    @Override
    public Component getTestComponent() {

        MDateField df = new MDateField();
        df.setCaption("Time initialised to beginning of the day");
        df.setInitialTimeMode(MDateField.InitialTimeMode.START_OF_DAY);
        df.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show(event.getProperty().getValue().toString());
            }
        });

        MDateField df2 = new MDateField();
        df2.setCaption("Time initialised to end of the day");
        df2.setInitialTimeMode(MDateField.InitialTimeMode.END_OF_DAY);
        df2.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show(event.getProperty().getValue().toString());
            }
        });

        return new MVerticalLayout(df, df2);

    }

}
