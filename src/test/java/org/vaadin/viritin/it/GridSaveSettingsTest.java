package org.vaadin.viritin.it;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;

import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class GridSaveSettingsTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        Label info = new Label();
        info.setValue("Hide and/or sort columns."
                + " Grid will save your settings in the browser cookies."
                + " And load them next time you open the view");

        MGrid<Person> table = new MGrid<>();
        table.setRows(
                new Person(1, "Some", "One", 13),
                new Person(2, "Arni", "Red", 83),
                new Person(3, "Sami", "Green", 42),
                new Person(4, "Togy", "Tilly", 23),
                new Person(5, "Billy", "Willy", 22),
                new Person(6, "Anna", "Bunny", 23),
                new Person(7, "Joe", "Black", 52)
        );
        makeGridColumnsHideable(table);
        makeGridOrderable(table);
        table.setColumnReorderingAllowed(true);
        table.attachSaveSettings("personsGrid");

        return new MVerticalLayout(info, table);
    }

    private void makeGridOrderable(MGrid table) {
        table.setColumnReorderingAllowed(true);

    }

    private void makeGridColumnsHideable(Grid grid) {
        for (Column c : grid.getColumns()) {
            c.setHidable(true);
        }
    }
}
