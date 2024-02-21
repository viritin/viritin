package org.vaadin.viritinv7;

import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.v7.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritinv7.fields.MTextField;
import org.vaadin.viritinv7.grid.MGrid;
import org.vaadin.viritin.it.AbstractTest;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * An example how filtering can be done in MGrid.
 * <p>
 * This example uses just in memory data, which is 
 * filtered if filter is set. The example just filters
 * on firstName, but the example can easily be extended
 * to support multiple properties.
 *
 * @author Matti Tahvonen
 */
public class GridFilteringWithStreams extends AbstractTest {

    private static final long serialVersionUID = 503365638639247756L;

    List<Person> originalListOfPersons = Service.getListOfPersons(100);
    private String firstNameFilter;
    MGrid<Person> g = new MGrid<>(Person.class)
            .withProperties("firstName", "lastName", "age", "addresses[1].street");

    @Override
    public Component getTestComponent() {

        Grid.HeaderRow filteringHeader = g.appendHeaderRow();

        // Add new TextFields to each column which filters the data from
        // that column
        String columnId = "firstName";
        TextField filter = new MTextField()
                .withFullWidth().withStyleName(ValoTheme.TEXTFIELD_TINY)
                .withInputPrompt("Filter");
        filter.addTextChangeListener(event -> {
            firstNameFilter = event.getText();
            listPersons();
        });
        filteringHeader.getCell(columnId).setComponent(filter);
        filteringHeader.getCell(columnId).setStyleName("filter-header");

        listPersons();

        return g;
    }

    private void listPersons() {
        if (StringUtils.isEmpty(firstNameFilter)) {
            g.setRows(originalListOfPersons);
        } else {
            List<Person> filtered = originalListOfPersons.stream()
                    .filter(p -> p.getFirstName().startsWith(firstNameFilter))
                    .collect(Collectors.toList());
            g.setRows(filtered);
        }
    }

}
