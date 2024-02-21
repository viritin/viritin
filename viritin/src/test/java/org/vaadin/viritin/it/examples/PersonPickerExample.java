package org.vaadin.viritin.it.examples;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import java.util.List;
import java.util.stream.Collectors;

import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.it.AbstractTest;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * An example code how to build a custom picker for an object while displaying the options
 * in a filterable data grid.
 * 
 * @author Matti Tahvonen
 */
public class PersonPickerExample extends AbstractTest {

    public static class PersonPicker extends CustomField<Person> {

        Label display = new Label();
        Button selectBtn = new Button("Choose...");
        MTextField filter = new MTextField().withPlaceholder("Filter list...");
        MGrid<Person> grid = new MGrid<>(Person.class);
        Window window = new Window();

        private Person person;
        private List<Person> listOfPersons;

        public PersonPicker() {
            window.setCaption("Select the desired person...");
            window.setContent(new MVerticalLayout(filter).expand(grid));
            window.setModal(true);
            window.setWidth("80%");
            window.setHeight("80%");

            filter.addTextChangeListener(e -> {
                // If you have a large list, use filtering with lazy DataProvider
                List<Person> filteredList = listOfPersons.stream()
                        .filter(p -> p.toString().toLowerCase().contains(e.getValue().toLowerCase()))
                        .collect(Collectors.toList());
                grid.setItems(filteredList);
            });

            grid.addItemClickListener(e -> {
                // set the value of custom component when user clicks a row
                setValue(e.getItem(), true);
                window.close();
                filter.clear();
            });
            grid.setSizeFull();
        }

        @Override
        protected Component initContent() {
            selectBtn.setDescription("Click to select person.");
            selectBtn.addClickListener(e -> {
                getUI().addWindow(window);
            });
            return new MHorizontalLayout(display, selectBtn).alignAll(Alignment.MIDDLE_LEFT);
        }

        private void setPersons(List<Person> listOfPersons) {
            // If you have a large list, use lazy DataProvider
            grid.setItems(listOfPersons);
            this.listOfPersons = listOfPersons;
        }

        @Override
        protected void doSetValue(Person value) {
            this.person = value;
            display.setValue(value.getFirstName() + " " + value.getLastName());
        }

        @Override
        public Person getValue() {
            return person;
        }

    }

    @Override
    public Component getTestComponent() {
        final List<Person> listOfPersons = Service.getListOfPersons(100);

        PersonPicker personPicker = new PersonPicker();
        personPicker.setPersons(listOfPersons);
        personPicker.setValue(listOfPersons.get(0));

        personPicker.addValueChangeListener(e -> Notification.show("New value selected!"));

        return new MVerticalLayout(
                personPicker
        );

    }

}
