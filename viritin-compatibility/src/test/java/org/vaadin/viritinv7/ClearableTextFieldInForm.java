package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;

import org.vaadin.viritinv7.fields.ClearableTextField;
import org.vaadin.viritinv7.form.AbstractForm;
import org.vaadin.viritin.it.AbstractTest;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class ClearableTextFieldInForm extends AbstractTest {

    private static final long serialVersionUID = 8480545478837182696L;

    public static class PersonForm extends AbstractForm<Person> {

        private static final long serialVersionUID = -2299890309080845494L;

        private final ClearableTextField firstName = new ClearableTextField()
                .withCaption("First name");
        private final ClearableTextField lastName = new ClearableTextField()
                .withCaption("Last name")
                .withFullWidth();

        public PersonForm() {
        }

        @Override
        protected Component createContent() {
            
            firstName.setWidth("300px");
            
            return new MVerticalLayout(firstName, lastName, getToolbar());
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
