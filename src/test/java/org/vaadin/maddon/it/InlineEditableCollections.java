package org.vaadin.maddon.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.fields.EnumSelect;
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
public class InlineEditableCollections extends AbstractTest {

    public static class AddressRow {

        EnumSelect type = new EnumSelect();
        MTextField street = new MTextField().withInputPrompt("street");
        MTextField city = new MTextField().withInputPrompt("city");
        MTextField zipCode = new MTextField().withInputPrompt("zip");

    }

    public static class PersonFormManualAddressAddition<Person> extends AbstractForm {

        private final InlineEditableCollection<Address> addresses
                = new InlineEditableCollection<Address>(Address.class,
                        AddressRow.class).withCaption("Addressess").setAllowNewElements(false);
        private Button add =  new MButton("Add row", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Address a = new Address();
                addresses.addElement(a);
            }
        });

        public PersonFormManualAddressAddition() {
            setEagerValidation(true);
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
    
        public static class PersonForm2<Person> extends AbstractForm {

        private final InlineEditableCollection<Address> addresses
                = new InlineEditableCollection<Address>(Address.class,
                        AddressRow.class).withCaption("Addressess");

        public PersonForm2() {
            setEagerValidation(true);
        }

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

        return form;
    }

}
