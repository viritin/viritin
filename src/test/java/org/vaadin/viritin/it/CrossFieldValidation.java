package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.Date;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MDateField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class CrossFieldValidation extends AbstractTest {

    @FieldMatch.List({
        @FieldMatch(first = "email", second = "verifyEmail", message = "Emails must match")
    })
    public static class Reservation {

        @NotNull(message = "Comment is required")
        @Size(min = 4, max = 15, message = "Comment must be longer than 3 and less than 15 characters")
        private String comment;

        @NotNull
        @Future
        private Date start;

        @NotNull
        private Date end;

        @NotNull
        private String email;

        @NotNull
        private String verifyEmail;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getVerifyEmail() {
            return verifyEmail;
        }

        public void setVerifyEmail(String verifyEmail) {
            this.verifyEmail = verifyEmail;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Date getStart() {
            return start;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public Date getEnd() {
            return end;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return "Reservation{" + "comment=" + comment + ", start=" + start + ", end=" + end + ", email=" + email + ", verifyEmail=" + verifyEmail + '}';
        }

    }

    public static class ReservationForm extends AbstractForm<Reservation> {

        private MTextField comment = new MTextField("Comment");
        private DateField start = new MDateField("Start")
                .withResolution(Resolution.MINUTE);
        private DateField end = new MDateField("End")
                .withResolution(Resolution.MINUTE);
        private MTextField email = new MTextField("Email");
        private MTextField verifyEmail = new MTextField("Verify email");
        private RichText errors = new RichText();

        public ReservationForm() {
            // In this test using via AbstractForm but can be used with raw
            // MBeanFieldGroup as well
            addValidator(
                    new MBeanFieldGroup.MValidator<Reservation>() {

                        @Override
                        public void validate(Reservation value) throws Validator.InvalidValueException {
                            // No null checks needed as this is not reached unless 
                            // field validators pass (which has @NotNull in this case
                            if (value.getStart().getTime() > value.getEnd().
                            getTime()) {
                                throw new Validator.InvalidValueException(
                                        "End time cannot be before start time!");
                            }
                        }
                    }
                    // configure the properties/fields where the error should be displayed.
                    // if your provide none, the error will be available in
                    // getFieldGroup().getBeanLevelValidationErrors()
                    //,"start", "end"
            );
        }

        @Override
        public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
            super.onFieldGroupChange(beanFieldGroup);

            // TODO consider a simpler better way to display top level JSR303 annotations
            // configurable built-in feature to AbstractField or something
            errors.setVisible(false);
            StringBuilder sb = new StringBuilder();
            Collection<String> errorMessages = getFieldGroup().getBeanLevelValidationErrors();
            if (!errorMessages.isEmpty()) {
                for (String e : errorMessages) {
                    sb.append(e);
                    sb.append("<br/>");
                }
                errors.setValue(sb.toString());
                errors.setStyleName(ValoTheme.LABEL_FAILURE);
                errors.setVisible(true);
            }
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(new Header("Edit reservation"), comment,
                    start, end, email, verifyEmail,
                    //getFieldGroup().getValidationStatusDisplay(),
                    errors,
                    getToolbar());
        }

    }

    @Override
    public Component getTestComponent() {
        final ReservationForm form = new ReservationForm();

        form.addValidityChangedListener(
                new AbstractForm.ValidityChangedListener<Reservation>() {
                    @Override
                    public void onValidityChanged(
                            AbstractForm.ValidityChangedEvent<Reservation> event) {
                                if (event.getComponent().isValid()) {
                                    Notification.show("The form is now valid!",
                                            Notification.Type.TRAY_NOTIFICATION);
                                } else {
                                    Notification.show(
                                            "Invalid values in form, clicking save is disabled!");
                                }
                            }
                });

        form.setSavedHandler(new AbstractForm.SavedHandler<Reservation>() {

            @Override
            public void onSave(Reservation entity) {
                Notification.show(entity.toString());
            }
        });

        form.setDeleteHandler(new AbstractForm.DeleteHandler<Reservation>() {

            @Override
            public void onDelete(Reservation entity) {
                Notification.show("Delete: " + entity.toString());
            }
        });
        form.setEntity(new Reservation());

        Button button = new Button(
                "Show fields that should be filled, but not touched yet");
        button.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Notification.show(form.getFieldGroup().
                        getFieldsWithInitiallyDisabledValidation().toString());
            }
        });

        return new MVerticalLayout(form, button);
    }

}
