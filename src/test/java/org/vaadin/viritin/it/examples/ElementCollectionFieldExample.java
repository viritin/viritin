package org.vaadin.viritin.it.examples;

import java.util.ArrayList;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.testdomain.Person;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;

@Theme("valo")
public class ElementCollectionFieldExample extends AbstractTest {

    public static class PersonRow {
        MTextField firstName = new MTextField();
        MTextField lastName = new MTextField();
        MTextField age = new MTextField();
    }

    @Override
    public Component getTestComponent() {
        ElementCollectionField<Person> ecf = new ElementCollectionField<Person>(Person.class, PersonRow.class);
        ecf.setValue(new ArrayList<Person>());
        return ecf;
    }

}
