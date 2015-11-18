package org.vaadin.viritin.it;

import com.vaadin.ui.Component;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
public class GridWithEditorRow extends AbstractTest {

    @Override
    public Component getTestComponent() {

        List<Person> listOfPersons = Service.getListOfPersons(100);
        final Person selectedDude = listOfPersons.get(2);

        MGrid<Person> g = new MGrid<Person>().withProperties("firstName",
                "lastName", "age");
        g.setRows(listOfPersons);
        g.setEditorEnabled(true);

        return g;
    }

}
