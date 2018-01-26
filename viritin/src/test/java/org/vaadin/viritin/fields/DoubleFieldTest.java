package org.vaadin.viritin.fields;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

public class DoubleFieldTest {

    private DoubleField fieldUnderTest;

    @Before
    public void setUp() throws Exception {
        fieldUnderTest = new DoubleField();
    }

    @Test
    public void testUserInputToValue_freshField() {
        assertThat(fieldUnderTest.getValue(), is((Double) null));
    }

    @Test
    public void testUserInputToValue() {
        fieldUnderTest.userInputToValue("2.0");
        assertThat(fieldUnderTest.getValue(), is(2.0));
    }

    @Test
    public void testUserInputToValue_changeValueToNull() {
        fieldUnderTest.userInputToValue("2.0");
        fieldUnderTest.userInputToValue("");
        assertThat(fieldUnderTest.getValue(), is((Integer) null));
    }

}
