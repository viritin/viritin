package org.vaadin.viritin.it;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.HeaderField;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.fields.LabelField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Address;
import org.vaadin.viritin.testdomain.Address.AddressType;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
public class EditPersonV8 extends AbstractTest {

    private static final long serialVersionUID = 8480545478837182696L;

    public static class AddressRow {

        EnumSelect<AddressType> type = new EnumSelect<>(AddressType.class);
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        // TODO try to make MBinder that configures basic converters automatically
        // e.g. if using basic MTextField for zipCode
        IntegerField zipCode = new IntegerField().withPlaceHolder("zip");
        {
            type.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
            // If you don't want toString presentation of your enums,
            // you can do whatwever you want here
            type.setItemCaptionGenerator( (AddressType option) -> {
                    return option.toString().toLowerCase();
                }
            );
        }
    }

    public static class PersonForm extends AbstractForm<Person> {

        private static final long serialVersionUID = -2299890309080845494L;

        private final MTextField firstName = new MTextField("Name")
                .withAutocompleteOff()
                .withAutoCorrectOff()
                .withAutoCapitalizeOff()
                .withSpellCheckOff();

        // TODO figure out what is wrong with IntegerField, not bound properly
        private final IntegerField age = new IntegerField("Age");

        private final HeaderField<Integer> id = new HeaderField<Integer>("ID");
        private final ElementCollectionField<Address, List<Address>> addresses = new ElementCollectionField<Address, List<Address>>(
                Address.class, AddressRow.class).withCaption("Addressess")
//                .addElementAddedListener(ElementCollections.addedListener)
//                .addElementRemovedListener(ElementCollections.removeListener)
                ;
//        private final MultiSelectTable<Group> groups = new MultiSelectTable<Group>().
//                withProperties("name")
//                .setOptions(Service.getAvailableGroups());
        public PersonForm() {
            super(Person.class);
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(id, firstName, age, addresses,
                    getToolbar());
        }

    }

    @Override
    public Component getTestComponent() {
        PersonForm form = new PersonForm();

        form.getBinder().addStatusChangeListener(event -> {
            if (!event.hasValidationErrors()) {
                Notification.show("The form is now valid!",
                        Notification.Type.TRAY_NOTIFICATION);
            } else {
                Notification.show(
                        "Invalid values in form, clicking save is disabled!");
            }
        });

        form.getBinder().addValueChangeListener(e -> {
            Notification.show("Value change:" + e.getComponent() + " " + e.getValue(),
                    Notification.Type.TRAY_NOTIFICATION);

        });

        Person p = Service.getPerson();
        Address address = new Address();
        address.setCity("Paimio");
        address.setStreet("Sampsala");
        address.setType(AddressType.Home);
        address.setZipCode(1234);
        p.getAddresses().add(address);
        form.setEntity(p);

        form.setSavedHandler(new AbstractForm.SavedHandler<Person>() {
            private static final long serialVersionUID = 1008970415395369248L;

            @Override
            public void onSave(Person entity) {
                Notification.show(entity.toString());
            }
        });

        form.setDeleteHandler(new AbstractForm.DeleteHandler<Person>() {
            private static final long serialVersionUID = -6298152846013943120L;

            @Override
            public void onDelete(Person entity) {
                Notification.show("Delete: " + entity.toString());
            }
        });

        Button openInPopup = new Button("Open in popup");
        openInPopup.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                final PersonForm form = new PersonForm();

                Person p = Service.getPerson();
                form.setEntity(p);

                form.setSavedHandler(new AbstractForm.SavedHandler<Person>() {
                    private static final long serialVersionUID = 1008970415395369248L;

                    @Override
                    public void onSave(Person entity) {
                        Notification.show(entity.toString());
                    }
                });

                form.setDeleteHandler(new AbstractForm.DeleteHandler<Person>() {
                    private static final long serialVersionUID = -6298152846013943120L;

                    @Override
                    public void onDelete(Person entity) {
                        Notification.show("Delete: " + entity.toString());
                    }
                });
                form.setResetHandler(new AbstractForm.ResetHandler<Person>() {
                    private static final long serialVersionUID = -1695108652595021734L;

                    @Override
                    public void onReset(Person entity) {
                        Notification.show("Nothing done");
                        form.getPopup().close();
                    }
                });
                form.openInModalPopup();

            }
        });

        return new MVerticalLayout(form, openInPopup);
    }

}
