package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.LabelField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Address;
import org.vaadin.viritin.testdomain.Dude;

/**
 * An example how to use some "nested properties" in form.
 * 
 * @author Matti Tahvonen
 */
@Theme("valo")
public class EditDude extends AbstractTest {


    public static class DudeForm extends AbstractForm<Dude> {

        private final MTextField firstName = new MTextField("Name");

        private final MTextField age = new MTextField("Age");

        LabelField<Integer> id = new LabelField<>(Integer.class)
                .withCaption("ID");
        
        @PropertyId("address.type")
        EnumSelect<Address.AddressType> type = new EnumSelect<>();
        @PropertyId("address.street")
        MTextField street = new MTextField().withInputPrompt("street");
        @PropertyId("address.city")
        MTextField city = new MTextField().withInputPrompt("city");
        @PropertyId("address.zipCode")
        MTextField zipCode = new MTextField().withInputPrompt("zip");
        
        public DudeForm() {
            setNestedProperties("address.street","address.city", "address.zipCode", "address.type");
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(id, firstName, age, new MHorizontalLayout(street, city, zipCode).withCaption(
                            "Address"),
                    getToolbar());
        }

    }

    @Override
    public Component getTestComponent() {
        DudeForm form = new DudeForm();

        Dude p = new Dude();
        p.setFirstName("Jorma");
        p.setAge(80);
        p.getAddress().setStreet("Ruukinkatu");
        form.setEntity(p);

        form.setSavedHandler(new AbstractForm.SavedHandler<Dude>() {

            @Override
            public void onSave(Dude entity) {
                Notification.show(entity.toString());
            }
        });

        return new MVerticalLayout(form);
    }

}
