package org.vaadin.viritin.it.issues;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
public class Issue131 extends AbstractTest {

    public class Member implements Serializable {

        private static final String MEMBER_CODE = "MEM";
        private static final long serialVersionUID = -4051512128345987991L;

        private Long id;

        private String reference;

        @NotNull(message = "validation.initials.required")
        private String initials;

        private String firstName;
        private String middleName;

        @NotNull(message = "validation.last.name.required")
        private String lastName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getInitials() {
            return initials;
        }

        public void setInitials(String initials) {
            this.initials = initials;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "Member{" + "id=" + id + ", reference=" + reference + ", initials=" + initials + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName + '}';
        }

    }

    Logger log = Logger.getLogger(getClass().getName());

    public class MemberForm2 extends AbstractForm<Member> {

        private static final long serialVersionUID = 2115224810575804578L;
        final MBeanFieldGroup<Member> fieldGroup;

        @PropertyId("initials")
        private MTextField initials = new MTextField()
                .withWidth(100.0f, Unit.PIXELS)
                .withInputPrompt("Initials");

        @PropertyId("middleName")
        private MTextField middleName = new MTextField()
                .withWidth(100.0f, Unit.PIXELS)
                .withInputPrompt("Middle");

        @PropertyId("lastName")
        private MTextField lastName = new MTextField("Last")
                .withFullWidth()
                .withInputPrompt("Last");

        public MemberForm2(final Member member) {
            fieldGroup = setEntity(member);
            log.info("Initials:" + member.getInitials());
            log.info("Middle name:" + member.getMiddleName());
            log.info("Last name: " + member.getLastName());
        }

        private void initFields() {
            initials = new MTextField()
                    .withWidth(100.0f, Unit.PIXELS)
                    .withInputPrompt("A.");
            middleName = new MTextField()
                    .withWidth(100.0f, Unit.PIXELS)
                    .withInputPrompt("Middle");
            lastName = new MTextField("Last name")
                    .withFullWidth()
                    .withInputPrompt("Bakker");
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(
                    new MFormLayout(
                            newButton(),
                            new MHorizontalLayout(initials, middleName).
                            withCaption("Name"),
                            lastName
                    ).withMargin(false).withFullWidth(),
                    getToolbar()
            ).withMargin(new MMarginInfo(false, true)).withFullWidth();
        }

        private Button newButton() {
            final Button button = new Button("Check validation");
            button.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    log.info("Form isValid(): " + isValid());
                    log.info("FieldGroup isValid(): " + fieldGroup.isValid());
                    log.info("FieldGroup isModified(): " + fieldGroup.
                            isModified());

                    final Collection<String> beanLevelValidationErrors = fieldGroup.
                            getBeanLevelValidationErrors();
                    if (CollectionUtils.isNotEmpty(beanLevelValidationErrors)) {
                        for (String beanLevelValidationError : beanLevelValidationErrors) {
                            log.info(beanLevelValidationError);
                        }
                    }

                    final Set<ConstraintViolation<Member>> constraintViolations = fieldGroup.
                            getConstraintViolations();
                    if (constraintViolations != null) {
                        log.info("getConstraintViolations() size: "
                                + constraintViolations.size());

                        for (ConstraintViolation<Member> constraintViolation : constraintViolations) {
                            log.info("Invalid value: " + constraintViolation.
                                    getInvalidValue() + " | Message: " + constraintViolation.
                                    getMessage() + " | Path: " + constraintViolation.
                                    getPropertyPath() + "\n"
                            );
                        }
                    }
                }
            });

            return button;
        }
    }

    @Override
    public Component getTestComponent() {
        Member m = new Member();
        m.initials = "MT";
        m.lastName = "Tahvonen";
        final MemberForm2 memberForm2 = new MemberForm2(m);
        memberForm2.setSavedHandler(new AbstractForm.SavedHandler<Member>() {
            private static final long serialVersionUID = -4100482203816246947L;

            @Override
            public void onSave(Member entity) {
                Notification.show(entity.toString());
            }
        });
        return memberForm2;
    }

}
