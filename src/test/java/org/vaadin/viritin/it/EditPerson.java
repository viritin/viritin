package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.CaptionGenerator;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.IntegerField;
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

    private static final long serialVersionUID = 8480545478837182696L;

    public static class AddressRow {

        EnumSelect<Address.AddressType> type = new EnumSelect<>();
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");

        {
            type.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
            // If you don't want toString presentation of your enums,
            // you can do whatwever you want here
            type.setCaptionGenerator(
                    new CaptionGenerator<Address.AddressType>() {
                        private static final long serialVersionUID = -5994389052707708278L;

                        @Override
                        public String getCaption(Address.AddressType option) {
                            return option.toString().toLowerCase();
                        }
                    });
        }
    }

    public static class PersonForm extends AbstractForm<Person> {

        private static final long serialVersionUID = -2299890309080845494L;

        private final MTextField firstName = new MTextField("Name")
                .withAutocompleteOff()
                .withAutoCorrectOff()
                .withAutoCapitalizeOff()
                .withSpellCheckOff()
                ;

        private final IntegerField age = new IntegerField("Age");

        private final LabelField<Integer> id = new LabelField<>(Integer.class)
                .withCaption("ID");
        private final ElementCollectionField<Address> addresses = new ElementCollectionField<>(
                Address.class, AddressRow.class).withCaption("Addressess")
                .addElementAddedListener(ElementCollections.addedListener)
                .addElementRemovedListener(ElementCollections.removeListener);

        private final MultiSelectTable<Group> groups = new MultiSelectTable<Group>().
                withProperties("name")
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

        form.addValidityChangedListener(
                new AbstractForm.ValidityChangedListener<Person>() {
                    private static final long serialVersionUID = -3688956596748617476L;
                    
                    @Override
                    public void onValidityChanged(
                            AbstractForm.ValidityChangedEvent<Person> event) {
                                if (event.getComponent().isValid()) {
                                    Notification.show("The form is now valid!",
                                            Notification.Type.TRAY_NOTIFICATION);
                                } else {
                                    Notification.show(
                                            "Invalid values in form, clicking save is disabled!");
                                }
                            }
                });

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
