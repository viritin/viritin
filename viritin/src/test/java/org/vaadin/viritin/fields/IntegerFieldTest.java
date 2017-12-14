package org.vaadin.viritin.fields;

import com.vaadin.ui.themes.ValoTheme;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class IntegerFieldTest {

    private IntegerField fieldUnderTest;

    @Before
    public void setUp() throws Exception {
        fieldUnderTest = new IntegerField();
        fieldUnderTest.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
    }

    @Test
    public void testUserInputToValue_freshField() {
        assertThat(fieldUnderTest.getValue(), is((Integer) null));
    }

    @Test
    public void testUserInputToValue() {
        fieldUnderTest.userInputToValue("2");
        assertThat(fieldUnderTest.getValue(), is(2));
    }

    @Test
    public void testUserInputToValue_changeValueToNull() {
        fieldUnderTest.userInputToValue("2");
        fieldUnderTest.userInputToValue("");
        assertThat(fieldUnderTest.getValue(), is((Integer) null));
    }

    @Test
    public void testTextFieldHasStyles() {
        assertThat("Underlying textfield is null", fieldUnderTest.tf, notNullValue());
        Matcher<String> hasStyle = containsString(ValoTheme.TEXTFIELD_INLINE_ICON);
        assertThat("Underlying textfield does not contain style", fieldUnderTest.tf.getStyleName(), hasStyle);
    }

}
