package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Address;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class ElementCollectionWithPopupEditor extends AbstractTest {

    public static class AddressRow {

        EnumSelect type = new EnumSelect();
        MTextField street = new MTextField().withInputPrompt("street");
//        MTextField city = new MTextField().withInputPrompt("city");
//        MTextField zipCode = new MTextField().withInputPrompt("zip");

    }

    public static class FullAddressForm extends AbstractForm<Address> {

        private static final long serialVersionUID = 8476434813427628969L;
        EnumSelect type = new EnumSelect("type");
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");

        public FullAddressForm() {
            setSaveCaption("Close");
        }

        @Override
        protected Component createContent() {
            return new MFormLayout(type, street, city, zipCode, getToolbar())
                    .withUndefinedWidth()
                    .withMargin(true);
        }
        
    }
    

    public static class PersonFormManualAddressAddition<Person> extends AbstractForm {

        private static final long serialVersionUID = -7517054537365246364L;
        private final ElementCollectionField<Address> addresses
                = new ElementCollectionField<>(Address.class,
                        AddressRow.class).withCaption("Addressess").
                setRequireVerificationForRemoving(true)
                .setAllowNewElements(false);

        private final Button add = new MButton("Add row", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Address a = new Address();
                addresses.addElement(a);
                addresses.editInPopup(a);
            }
        });

        public PersonFormManualAddressAddition() {
            addresses.setPopupEditor(new FullAddressForm());
        }

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

            private static final long serialVersionUID = 5522176287160062623L;

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
