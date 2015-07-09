package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import java.util.List;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class PerformanceListContainer extends AbstractTest {
    protected static final List<Person> LIST_OF_PERSONS = Service.getListOfPersons(10000);
 
    @Override
    public Component getTestComponent() {
        MTable<Person> mTable = new MTable<Person>(Person.class).withProperties("firstName", "lastName");
        mTable.setBeans(LIST_OF_PERSONS);
        return mTable;
    }
    
}
