package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.AbstractElementCollection;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.ElementCollectionTable;
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
@Theme("valo")
public class ElementCollections extends AbstractTest {

    static AbstractElementCollection.ElementAddedListener<Address> addedListener = new AbstractElementCollection.ElementAddedListener<Address>() {

        @Override
        public void elementAdded(
                AbstractElementCollection.ElementAddedEvent<Address> e) {
            Notification.show("Added row: " + e.getElement());
        }
    };

    static AbstractElementCollection.ElementRemovedListener<Address> removeListener = new AbstractElementCollection.ElementRemovedListener<Address>() {

        @Override
        public void elementRemoved(
                AbstractElementCollection.ElementRemovedEvent<Address> e) {
            Notification.show("Removed row: " + e.getElement());
        }
    };

    public static class AddressRow {

        EnumSelect type = new EnumSelect();
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");

    }

    public static class AddressRow2 {

        NativeSelect type = new NativeSelect();
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");

    }

    public static class PersonFormManualAddressAddition<Person> extends AbstractForm {

        private final ElementCollectionField<Address> addresses
                = new ElementCollectionField<Address>(Address.class,
                        AddressRow2.class).withCaption("Addressess").
                setAllowNewElements(false).
                setAllowRemovingItems(false).
                addElementAddedListener(addedListener).
                addElementRemovedListener(removeListener);

        private Button add = new MButton("Add row", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Address a = new Address();
                addresses.addElement(a);
            }
        });
        private Button remove = new MButton("Remove first",
                new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        Address a = (Address) addresses.getValue().iterator().
                        next();
                        addresses.removeElement(a);
                    }
                });

        @Override
        protected Component createContent() {

            /**
             * In simple cases the editor row instance don't need to be
             * configured or it can be instantiated automatically with empty
             * parameter constructor. This would work here as well, but as an
             * example lets override the instantiation strategy, where in real
             * life you could e.g. populate options to selects from your
             * backend.
             */
            addresses.setEditorInstantiator(
                    new AbstractElementCollection.Instantiator<AddressRow2>() {

                        @Override
                        public AddressRow2 create() {
                            AddressRow2 addressRow = new AddressRow2();
                            // Not using EnumSelect here as an example, so 
                            // populating manually.
                            addressRow.type.addItems((Object[]) AddressType.values());
                            return addressRow;
                        }
                    });

            return new MVerticalLayout(
                    addresses,
                    add,
                    remove,
                    getToolbar()
            );
        }

    }

    public static class PersonForm2<Person> extends AbstractForm {

        private final ElementCollectionTable<Address> addresses
                = new ElementCollectionTable<Address>(Address.class,
                        AddressRow.class).withCaption(
                        "Addressess").addElementAddedListener(addedListener).
                addElementRemovedListener(removeListener).setPropertyHeader("type", "ABC").setPropertyHeader("city", "CITY");

        @Override
        protected Component createContent() {
            return new MVerticalLayout(
                    addresses,
                    getToolbar()
            );
        }

    }

    @Override
    public Component getTestComponent() {
        PersonFormManualAddressAddition form = new PersonFormManualAddressAddition();

        Person p = Service.getPerson();
        form.setEntity(p);

        form.setSavedHandler(new AbstractForm.SavedHandler<Person>() {

            @Override
            public void onSave(Person entity) {
                Notification.show(entity.toString());
            }
        });

        Person p2 = Service.getPerson();
        PersonForm2<Person> form2 = new PersonForm2<Person>();
        form2.setEntity(p2);

        form2.setSavedHandler(new AbstractForm.SavedHandler<Person>() {

            @Override
            public void onSave(Person entity) {
                Notification.show(entity.toString());
            }
        });

        return new MVerticalLayout(form, form2);
    }

    @Override
    protected void setup() {
        super.setup();
        content.setSizeUndefined();
    }

}
