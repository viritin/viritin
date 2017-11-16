package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.shared.ui.datefield.Resolution;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.DateField;
import com.vaadin.ui.Notification;
import java.util.Date;
import java.util.Locale;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.it.FieldMatch;
import org.vaadin.viritinv7.MBeanFieldGroup;
import org.vaadin.viritinv7.fields.EnumSelect;
import org.vaadin.viritinv7.fields.MDateField;
import org.vaadin.viritinv7.fields.MTextField;
import org.vaadin.viritinv7.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class CrossFieldValidation extends AbstractTest {
    
    public static interface CustomGroup {
        
    }

    public enum TestEnum {

        FOO, BAR
    }

    @FieldMatch.List({
        @FieldMatch(first = "email", second = "verifyEmail", message = "{YourMsgKey}"
                , groups = CustomGroup.class
        )
    }
    )
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

        @NotNull
        private TestEnum testEnum = TestEnum.FOO;

        public TestEnum getTestEnum() {
            return testEnum;
        }

        public void setTestEnum(TestEnum testEnum) {
            this.testEnum = testEnum;
        }

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

        private static final long serialVersionUID = -8641811916424288855L;
        private final MTextField comment = new MTextField("Comment");
        private final DateField start = new MDateField("Start")
                .withResolution(Resolution.MINUTE);
        private final DateField end = new MDateField("End")
                .withResolution(Resolution.MINUTE);
        private final MTextField email = new MTextField("Email");
        private final MTextField verifyEmail = new MTextField("Verify email");
        private final EnumSelect testEnum = new EnumSelect("Test enum");

        public ReservationForm() {
            // In this test using via AbstractForm but can be used with raw
            // MBeanFieldGroup as well
            addValidator(new MValidatorImpl()
            // configure the properties/fields where the error should be displayed.
            // if your provide none, the error will be available in
            // getFieldGroup().getBeanLevelValidationErrors()
            //,start, end
            );

            /* cross field validations, both MValidator types and JSR303 validators,
             * can also be configured to report the error to a specific component */

            /* This puts MValidatorImpl errors (date check) to comment, just for fun ;-) */
            // setValidationErrorTarget(MValidatorImpl.class, comment);
            /* Makes the FieldMatch validation violation (configured here to check to 
             * email fields) to be shown on verifyEmail ui component.
             */
            // setValidationErrorTarget(FieldMatch.class, verifyEmail);
            
            /* Bean level JSR303 validators supports validation groups. */
            setValidationGroups(CustomGroup.class);
            setLocale(new Locale("fi"));
        }

        @Override
        protected Component createContent() {

            return new MVerticalLayout(new Header("Edit reservation"), comment,
                    testEnum,
                    start, end, email, verifyEmail,
                    getConstraintViolationsDisplay(),
                    getToolbar()).withMargin(false);
        }

        public static class MValidatorImpl implements
                MBeanFieldGroup.MValidator<Reservation> {

            private static final long serialVersionUID = 7773475835262158065L;

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

    }

    @Override
    protected void setup() {
        super.setup();
        content.setHeightUndefined();
    }

    @Override
    public Component getTestComponent() {
        final ReservationForm form = new ReservationForm();

        form.addValidityChangedListener(
                new AbstractForm.ValidityChangedListener<Reservation>() {
                    private static final long serialVersionUID = 5430174280044587676L;

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

            private static final long serialVersionUID = 382987534900227025L;

            @Override
            public void onSave(Reservation entity) {
                Notification.show(entity.toString());
            }
        });

        form.setDeleteHandler(new AbstractForm.DeleteHandler<Reservation>() {

            private static final long serialVersionUID = 427652068306119517L;

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
