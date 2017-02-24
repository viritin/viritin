package org.vaadin.viritin;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Locale;

import javax.validation.constraints.NotNull;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.v7.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Field;
import com.vaadin.v7.ui.TextField;

/**
 * Created by marco on 06/05/16.
 */
public class MBeanFieldGroupTest {

    private Locale defaultLocale;

    @Before
    public void storeDefaultLocale() {
        defaultLocale = Locale.getDefault();
    }

    @After
    public void restoreDefaultLocale() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void notNullAnnotatedFieldsShouldHaveInterpolatedErrorMessage() {
        // Force en_US as default for test purpose
        Locale.setDefault(Locale.US);

        MBeanFieldGroup fieldGroup = new MBeanFieldGroup<>(Tester.class);
        Field<?> defaultMessageField = fieldGroup.buildAndBind("defaultMessage");
        Field<?> customMessageKeyField = fieldGroup.buildAndBind("customMessageKey");
        Field<?> customMessageField = fieldGroup.buildAndBind("customMessage");
        fieldGroup.configureMaddonDefaults();
        assertThat(defaultMessageField.getRequiredError(), equalTo("may not be null"));
        assertThat(customMessageKeyField.getRequiredError(), equalTo("Emails must match!"));
        assertThat(customMessageField.getRequiredError(), equalTo("Custom message"));
    }

    @Test
    public void notNullAnnotatedFieldsShouldHaveInterpolatedErrorMessageWithLocale() {
        Locale locale = Locale.ITALIAN;
        MBeanFieldGroup fieldGroup = new MBeanFieldGroup<>(Tester.class);
        Field<?> defaultMessageField = fieldGroup.buildAndBind("defaultMessage");
        Field<?> customMessageKeyField = fieldGroup.buildAndBind("customMessageKey");
        Field<?> customMessageField = fieldGroup.buildAndBind("customMessage");
        withLocale(locale, defaultMessageField, customMessageField, customMessageKeyField);
        fieldGroup.configureMaddonDefaults();
        assertThat(defaultMessageField.getRequiredError(), equalTo("Non deve essere nullo"));
        assertThat(customMessageKeyField.getRequiredError(), equalTo("Gli indirizzi email devono corrispondere!"));
        assertThat(customMessageField.getRequiredError(), equalTo("Custom message"));
    }
    
    @Test
    public void  readOnlyFieldsShouldNotBeMadeRequired() {
        
        MBeanFieldGroup<Tester> bfg = new MBeanFieldGroup<>(Tester.class);
        
        TextField readOnlyField = new TextField();
        readOnlyField.setReadOnly(true);
        TextField basic = new TextField();
        bfg.bind(readOnlyField, "defaultMessage");
        bfg.bind(basic, "customMessageKey");
        bfg.configureMaddonDefaults();
        
        Assert.assertFalse("Read only field don't need to be marked required", readOnlyField.isRequired());
        Assert.assertTrue("Editable notnull properties should make fields required", basic.isRequired());
        
    }

    @Test
    public void validateOnlyBoundFields() {

        Tester2 tester = new Tester2();
        tester.setDefaultMessage("test"); //sets only one of NotNull values
        TesterForm form = new TesterForm();
        MBeanFieldGroup<Tester2> fieldGroup = BeanBinder.bind(tester, form);

        Assert.assertTrue(fieldGroup.isValid());

        fieldGroup.setValidateOnlyBoundFields(false); //tells that all properties should be validated
        Assert.assertFalse(fieldGroup.isValid());
    }

    private void withLocale(Locale locale, Field<?>... fields) {
        Arrays.stream(fields).map(AbstractField.class::cast).forEach(f -> f.setLocale(locale));
    }

    public static class Tester {

        @NotNull
        private String defaultMessage;

        @NotNull(message = "{YourMsgKey}")
        private String customMessageKey;

        @NotNull(message = "Custom message")
        private String customMessage;

        public String getCustomMessage() {
            return customMessage;
        }

        public String getCustomMessageKey() {
            return customMessageKey;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }
    }

    public static class Tester2 {

        @NotNull
        private String defaultMessage;

        @NotNull(message = "{YourMsgKey}")
        private String customMessageKey;

        @NotNull(message = "Custom message")
        private String customMessage;

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public void setDefaultMessage(String defaultMessage) {
            this.defaultMessage = defaultMessage;
        }

        public String getCustomMessageKey() {
            return customMessageKey;
        }

        public void setCustomMessageKey(String customMessageKey) {
            this.customMessageKey = customMessageKey;
        }

        public String getCustomMessage() {
            return customMessage;
        }

        public void setCustomMessage(String customMessage) {
            this.customMessage = customMessage;
        }

    }

    public class TesterForm extends AbstractForm<Tester2> {

        private static final long serialVersionUID = -3550932858549479910L;

        private MTextField defaultMessage;

        @Override
        protected Component createContent() {
            defaultMessage = new MTextField("default message");
            return new MVerticalLayout(defaultMessage, getToolbar());
        }

    }
}
