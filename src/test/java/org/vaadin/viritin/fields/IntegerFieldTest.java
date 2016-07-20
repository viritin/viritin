package org.vaadin.viritin.fields;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

public class IntegerFieldTest {

    private IntegerField fieldUnderTest;

    @Before
    public void setUp() throws Exception {
        fieldUnderTest = new IntegerField();
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

}
