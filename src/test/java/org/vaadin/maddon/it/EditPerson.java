package org.vaadin.maddon.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.maddon.fields.InlineEditableCollection;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.fields.MultiSelectTable;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.maddon.testdomain.Address;
import org.vaadin.maddon.testdomain.Group;
import org.vaadin.maddon.testdomain.Person;
import org.vaadin.maddon.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class EditPerson extends AbstractTest {

    public static class AddressRow {
        MTextField type = new MTextField().withInputPrompt("type");
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");
    }

    public static class PersonForm<Person> extends AbstractForm {

        private MTextField firstName = new MTextField("Name");

        private final InlineEditableCollection<Address> addresses
                = new InlineEditableCollection<Address>(Address.class,
                        AddressRow.class).withCaption("Addressess");

        private final MultiSelectTable<Group> groups = new MultiSelectTable<Group>().
                withProperties("name")
                .setOptions(Service.getAvailableGroups());

        public PersonForm() {
            setEagerValidation(true);
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(firstName, addresses, groups, getToolbar());
        }

    }

    @Override
    public Component getTestComponent() {
        PersonForm form = new PersonForm();

        Person p = Service.getPerson();
        form.setEntity(p);

        form.setSavedHandler(new AbstractForm.SavedHandler<Person>() {

            @Override
            public void onSave(Person entity) {
                Notification.show(entity.toString());
            }
        });

        return form;
    }

}
