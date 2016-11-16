package org.vaadin.viritin.it;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * MGrid use a "filterable" version of ListContainer, although
 * it is quite much against the "containerless" philosophy. Thus
 * it supports copy pasted example of filtering header from 
 * sampler.
 * <p>
 * The suggested method to implement filtering is to do it
 * yourself, e.g. using Java 8 stream API and passing the 
 * filtered list to MGrid using setRows method. This way you'll
 * have better control on the filtering process. You can also 
 * use this suggested method with lazyloading, so that your 
 * lazy loading strategies takes the active filters into account.
 * 
 * @author Matti Tahvonen
 */
public class GridFiltering extends AbstractTest {

    private static final long serialVersionUID = 503365638639247756L;

    @Override
    public Component getTestComponent() {

        List<Person> listOfPersons = Service.getListOfPersons(100);

        MGrid<Person> g = new MGrid<>(Person.class);
        g.withProperties("firstName", "lastName", "age", "addresses[1].street");

        // Awesome, now the API actually tells you what you should pass as 
        // parameters and what you'll get as return type.
        g.setRows(listOfPersons);

        Grid.HeaderRow filteringHeader = g.appendHeaderRow();

        // Add new TextFields to each column which filters the data from
        // that column
        String columnId = "firstName";
        TextField filter = new TextField();
        filter.setWidth("100%");
        filter.addStyleName(ValoTheme.TEXTFIELD_TINY);
        filter.setInputPrompt("Filter");
        filter.addTextChangeListener(new TextChangeListener() {

            SimpleStringFilter filter = null;

            @Override
            public void textChange(TextChangeEvent event) {
                Filterable f = (Filterable) g.getContainerDataSource();

                // Remove old filter
                if (filter != null) {
                    f.removeContainerFilter(filter);
                }

                // Set new filter for the "Name" column
                filter = new SimpleStringFilter(columnId, event.getText(),
                        true, true);
                f.addContainerFilter(filter);

                g.cancelEditor();
            }
        });
        filteringHeader.getCell(columnId).setComponent(filter);
        filteringHeader.getCell(columnId).setStyleName("filter-header");

        return g;
    }

    
}
