package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyComboBoxUsage;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.LazyComboBox;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;

import org.vaadin.viritin.fields.MCheckBox;

@Theme("valo")
public class LazyComboBoxChangeLoadStrategy extends AbstractTest {

    @Override
    public Component getTestComponent() {

        final LazyComboBoxUsage.LazyService service = new LazyComboBoxUsage.LazyService();

        final LazyComboBox.FilterablePagingProvider filterablePagingProvider = (LazyComboBox.FilterablePagingProvider) (int firstRow, String filter) -> {
            System.err.
                    println("find entities " + firstRow + " f: " + filter);
            return service.findPersons(filter, firstRow,
                    10);
        };
        final LazyComboBox.FilterableCountProvider filterableCountProvider;
        filterableCountProvider = (String filter) -> {
            System.err.println("size " + filter);
            return service.countPersons(filter);
        };
        
        final LazyComboBox.FilterablePagingProvider youngPagingProvider = (LazyComboBox.FilterablePagingProvider) (int firstRow, String filter) -> {
            System.err.
                    println("find young entities " + firstRow + " f: " + filter);
            return service.findYoungPersons(filter, firstRow,
                    10);
        };
        final LazyComboBox.FilterableCountProvider youngCountProvider = (String filter) -> {
            System.err.println("size of young " + filter);
            return service.countYoungPersons(filter);
        };

        final LazyComboBox<Person> cb = new LazyComboBox<>(Person.class)
                .setCaptionGenerator((Person option) -> option.getFirstName() + " " + option.
                        getLastName() + " (age: " + option.getAge() + ")").withWidth("300px");

        cb.addMValueChangeListener(event -> {
            Notification.show("Selected value :" + event.getValue());
        });

        CheckBox checkBox = new MCheckBox("Teenagers or younger only");
        checkBox.addValueChangeListener(e -> {
            Boolean youngOnly = checkBox.getValue();
            if (youngOnly) {
                cb.loadFrom(youngPagingProvider, youngCountProvider, 10);
            } else {
                cb.loadFrom(filterablePagingProvider, filterableCountProvider,10);
            }
            cb.setValue(null);
        });
        checkBox.setValue(true);

        return new MVerticalLayout(checkBox, cb);
    }

}
