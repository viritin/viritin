package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.LabelField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MultiSelectTable;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Address;
import org.vaadin.viritin.testdomain.Group;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class EditPerson extends AbstractTest {

    public static class AddressRow {

        EnumSelect type = (EnumSelect) new EnumSelect();
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");

        {
            type.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        }
    }

    public static class PersonForm extends AbstractForm<Person> {

        private MTextField firstName = new MTextField("Name");

        private MTextField age = new MTextField("Age");

        LabelField<Integer> id = new LabelField<Integer>(Integer.class)
                .withCaption("ID");
        private final ElementCollectionField<Address> addresses = new ElementCollectionField<Address>(
                Address.class, AddressRow.class).withCaption("Addressess")
                .addElementAddedListener(ElementCollections.addedListener)
                .addElementRemovedListener(ElementCollections.removeListener);

        private final MultiSelectTable<Group> groups = new MultiSelectTable<Group>()
                .withProperties("name")
                .setOptions(Service.getAvailableGroups());

        public PersonForm() {
            addresses.setReadOnly(true);
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(id, firstName, age, addresses, groups,
                    getToolbar());
        }

    }

    @Override
    public Component getTestComponent() {
        PersonForm form = new PersonForm();

        form.addValidityChangedListener(new AbstractForm.ValidityChangedListener<Person>() {
            @Override
            public void onValidityChanged(AbstractForm.ValidityChangedEvent<Person> event) {
                if(event.getComponent().isValid()) {
                    Notification.show("The form is now valid!",
                            Notification.Type.TRAY_NOTIFICATION);
                } else {
                    Notification.show("Invalid values in form, clicking save is disabled!");
                }
             }
        });

        Person p = Service.getPerson();
        form.setEntity(p);

        form.setSavedHandler(new AbstractForm.SavedHandler<Person>() {

            @Override
            public void onSave(Person entity) {
                Notification.show(entity.toString());
            }
        });

        form.setDeleteHandler(new AbstractForm.DeleteHandler<Person>() {

            @Override
            public void onDelete(Person entity) {
                Notification.show("Delete: " + entity.toString());
            }
        });

        return form;
    }

}
