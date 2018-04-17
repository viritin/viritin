package org.vaadin.viritin.fields;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class HeaderFieldTest {
   
    private HeaderField<String> headerField;
    
    @Before
    public void setUp() throws Exception {
        headerField = new HeaderField<String>();
    }

    @Test
    public void testUserInputToValue_freshField() {
        assertThat(headerField.getValue(), is((String)null));
    }

    @Test
    public void testUserInputToValue() {
        headerField.setValue("header");
        headerField.setHeaderLevel(1);
        assertThat(headerField.getValue(), is("header"));
    }

}
