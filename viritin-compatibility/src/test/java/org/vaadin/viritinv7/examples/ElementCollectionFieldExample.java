package org.vaadin.viritinv7.examples;

import java.util.ArrayList;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritinv7.fields.ElementCollectionField;
import org.vaadin.viritinv7.fields.MTextField;
import org.vaadin.viritin.testdomain.Person;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;

@Theme("valo")
public class ElementCollectionFieldExample extends AbstractTest {

    private static final long serialVersionUID = 7350047443363548232L;

    public static class PersonRow {
        MTextField firstName = new MTextField();
        MTextField lastName = new MTextField();
        MTextField age = new MTextField();
    }

    @Override
    public Component getTestComponent() {
        ElementCollectionField<Person> ecf = new ElementCollectionField<>(Person.class, PersonRow.class);
        ecf.setValue(new ArrayList<>());
        return ecf;
    }

}
