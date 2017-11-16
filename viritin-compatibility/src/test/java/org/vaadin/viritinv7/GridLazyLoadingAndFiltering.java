package org.vaadin.viritinv7;

import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Grid;
import com.vaadin.v7.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritinv7.fields.MTextField;
import org.vaadin.viritinv7.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * And example how to use filtering with lazy loading
 * backend and MGrid.
 * 
 * @author Matti Tahvonen
 */
public class GridLazyLoadingAndFiltering extends AbstractTest {

    private static final long serialVersionUID = 7741969263616707523L;
    private String firstNameFilter = "";
    private LazyList.CountProvider countProvider;
    private LazyList.PagingProvider<Person> pagingProvider;

    @Override
    public Component getTestComponent() {
        pagingProvider = new LazyList.PagingProvider<Person>() {
            private static final long serialVersionUID = -9072230332041322210L;
            
            @Override
            public List<Person> findEntities(int firstRow) {
                return Service.findByFirstName(firstNameFilter, firstRow,
                        LazyList.DEFAULT_PAGE_SIZE);
            }
        };
        countProvider = new LazyList.CountProvider() {
            private static final long serialVersionUID = -6915320247020779461L;
            
            @Override
            public int size() {
                return (int) Service.countByFirstName(firstNameFilter);
            }
        };

        MGrid<Person> g = new MGrid<>(Person.class)
                .lazyLoadFrom(pagingProvider, countProvider);

        Grid.HeaderRow filteringHeader = g.appendHeaderRow();

        // Add new TextFields to each column which filters the data from
        // that column
        String columnId = "firstName";
        TextField filter = new MTextField()
                .withFullWidth().withStyleName(ValoTheme.TEXTFIELD_TINY)
                .withInputPrompt("Filter");
        filter.addTextChangeListener(event -> {
            firstNameFilter = event.getText();
            g.refreshRows();
        });
        filteringHeader.getCell(columnId).setComponent(filter);
        filteringHeader.getCell(columnId).setStyleName("filter-header");
        
        return g;
    }
}