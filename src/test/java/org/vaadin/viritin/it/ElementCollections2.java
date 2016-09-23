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
public class ElementCollections2 extends AbstractTest {

    public static class AddressRow {

        EnumSelect type = new EnumSelect();
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");

    }

    public static class PersonFormManualAddressAddition<Person> extends AbstractForm {

        private final ElementCollectionField<Address> addresses
                = new ElementCollectionField<>(Address.class,
                        AddressRow.class).withCaption("Addressess").
                setAllowRemovingItems(false).
                setAllowNewElements(false);

        private final Button add = new MButton("Add row", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Address a = new Address();
                addresses.addElement(a);
            }
        });

        @Override
        protected Component createContent() {


            return new MVerticalLayout(
                    addresses,
                    add,
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


        return new MVerticalLayout(form);
    }

    @Override
    protected void setup() {
        super.setup();
        content.setSizeUndefined();
    }

}
