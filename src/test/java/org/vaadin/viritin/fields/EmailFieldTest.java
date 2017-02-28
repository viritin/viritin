package org.vaadin.viritin.fields;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.vaadin.shared.ui.ValueChangeMode;

public class EmailFieldTest {

    @Test
    public void test_fluent() {
        EmailField field = new EmailField();

        assertThat(field.getValueChangeMode(),
                is(equalTo(ValueChangeMode.LAZY)));
        
        assertThat(
                new EmailField().withValueChangeMode(ValueChangeMode.BLUR)
                        .getValueChangeMode(),
                is(equalTo(ValueChangeMode.BLUR)));
    }

}
