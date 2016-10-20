package org.vaadin.viritin;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vaadin.viritin.testdomain.Address;

import javax.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Assert;
import static org.junit.Assert.assertThat;

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
}
