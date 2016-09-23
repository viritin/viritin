package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.fields.LabelField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class EditPersonWithDisabledField extends AbstractTest {
    
    
    public class CustomPerson extends Person {
        
        private final String readOnlyFieldInPojo = "fixedValue";

        public String getReadOnlyFieldInPojo() {
            return readOnlyFieldInPojo;
        }
        
    }

    public static class PersonForm extends AbstractForm<CustomPerson> {

        private static final long serialVersionUID = -2988976182433301844L;
        private final MTextField firstName = new MTextField("Name");
        
        private final MTextField readOnlyFieldInPojo = new MTextField("Should be RO");

        private final IntegerField age = new IntegerField("Age");

        private final LabelField<Integer> id = new LabelField<>(Integer.class)
                .withCaption("ID");

        public PersonForm() {
            firstName.setReadOnly(true);
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(id, firstName, readOnlyFieldInPojo, age,
                    getToolbar());
        }

    }

    @Override
    public Component getTestComponent() {
        PersonForm form = new PersonForm();

        form.addValidityChangedListener(
                new AbstractForm.ValidityChangedListener<CustomPerson>() {
                    private static final long serialVersionUID = -8159920971526151534L;

                    @Override
            public void onValidityChanged(
                    AbstractForm.ValidityChangedEvent<CustomPerson> event) {
                if (event.getComponent().isValid()) {
                    Notification.show("The form is now valid!",
                            Notification.Type.TRAY_NOTIFICATION);
                } else {
                    Notification.show(
                            "Invalid values in form, clicking save is disabled!");
                }
            }
        });

        CustomPerson p = new CustomPerson();
        p.setFirstName("RO in form");
        p.setAge(13);
        form.setEntity(p);

        form.setSavedHandler(new AbstractForm.SavedHandler<CustomPerson>() {

            private static final long serialVersionUID = 722131410583777207L;

            @Override
            public void onSave(CustomPerson entity) {
                Notification.show(entity.toString());
            }
        });

        return new MVerticalLayout(form);
    }

}
