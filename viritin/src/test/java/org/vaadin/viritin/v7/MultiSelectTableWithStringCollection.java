package org.vaadin.viritin.v7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TextField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.v7.MBeanFieldGroup;
import org.vaadin.viritin.v7.fields.MultiSelectTable;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.BrowserCookie;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MultiSelectTableWithStringCollection extends AbstractTest {
    
    public static class Beani {
        
        private List<String> strings = new ArrayList<>();

        public List<String> getStrings() {
            return strings;
        }

        public void setStrings(List<String> strings) {
            this.strings = strings;
        }

        @Override
        public String toString() {
            return "Beani{" + "strings=" + strings + '}';
        }
        
    }
    
    String[] options = new String[] {"Foo", "Bar", "Car"};

    MultiSelectTable strings = new MultiSelectTable();
        
    @Override
    public Component getTestComponent() {
        
        strings.setOptions(Arrays.asList(options));
        strings.withColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
        strings.withRowHeaderMode(Table.RowHeaderMode.ID);
        strings.withProperties();
        
        final Beani beani = new Beani();
        
        MBeanFieldGroup.bindFieldsUnbuffered(beani, this);
        
        
        Button showValue = new Button("show value", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show(beani.toString());
            }
        });
        
        return new MVerticalLayout(strings, showValue);
    }

}
