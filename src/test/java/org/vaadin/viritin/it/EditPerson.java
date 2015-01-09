package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.ElementCollectionField;
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

    public static class PersonForm<Person> extends AbstractForm {

        private MTextField firstName = new MTextField("Name");

        private final ElementCollectionField<Address> addresses
                = new ElementCollectionField<Address>(Address.class,
                        AddressRow.class).withCaption("Addressess")
                .addElementAddedListener(ElementCollections.addedListener)
                .addElementRemovedListener(ElementCollections.removeListener)
                ;

        private final MultiSelectTable<Group> groups = new MultiSelectTable<Group>().
                withProperties("name")
                .setOptions(Service.getAvailableGroups());

        public PersonForm() {
            setEagerValidation(true);
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(firstName, addresses, groups,
                    getToolbar());
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
