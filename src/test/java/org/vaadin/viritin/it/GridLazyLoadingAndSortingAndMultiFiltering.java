package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanComparator;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class GridLazyLoadingAndSortingAndMultiFiltering extends AbstractTest {

    ServiceLayerExample service = new ServiceLayerExample();

    TextField firstNameFilter = new MTextField().withInputPrompt("first name filter");
    TextField lastNameFilter = new MTextField().withInputPrompt("last name filter");
    Button filterBtn = new Button("Apply filters");

    @Override
    public Component getTestComponent() {

        final MGrid<Person> g = new MGrid<>(Person.class);

        Grid.HeaderRow headerRow = g.addHeaderRowAt(0);
        Grid.HeaderCell firstNameCell = headerRow.getCell("firstName");
        firstNameCell.setComponent(firstNameFilter);
        Grid.HeaderCell lastNameCell = headerRow.getCell("lastName");
        lastNameCell.setComponent(lastNameFilter);

        g.lazyLoadFrom(
                new SortableLazyList.SortablePagingProvider<Person>() {
            private static final long serialVersionUID = 8990276045925275684L;

            @Override
            public List<Person> findEntities(int firstRow, boolean sortAscending,
                    String sortProperty) {
                return service.findPersons(firstNameFilter.getValue(), lastNameFilter.getValue(), sortProperty, sortAscending, firstRow, LazyList.DEFAULT_PAGE_SIZE);
            }
        },
                new LazyList.CountProvider() {

            private static final long serialVersionUID = 6575441260380762210L;

            @Override
            public int size() {
                return service.size(firstNameFilter.getValue(), lastNameFilter.getValue());
            }
        }
        );

        filterBtn.addClickListener(e -> {
            g.refreshRows();
        });

        return new MVerticalLayout(filterBtn, g);
    }

    /**
     * An example service layer. The data is here just in memory, which we'll
     * sort/filter in the methods, but in real world case you'd of course make
     * e.g. DB queries based on the parameters.
     */
    public static class ServiceLayerExample {

        final List<Person> orig = Service.getListOfPersons(1000);

        public List<Person> findPersons(String firstNameFilter, String lastNameFilter, String sortProperty, boolean sortAscending, int firstRow, int DEFAULT_PAGE_SIZE) {
            List<Person> listOfPersons = new ArrayList<>(orig);
            listOfPersons = filter(listOfPersons, firstNameFilter, lastNameFilter);
            if (sortProperty != null) {
                Collections.sort(listOfPersons, new BeanComparator<>(
                        sortProperty));
                if (!sortAscending) {
                    Collections.reverse(listOfPersons);
                }
            }
            int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
            if (last > listOfPersons.size()) {
                last = listOfPersons.size();
            }
            return new ArrayList<>(listOfPersons.subList(firstRow,
                    last));

        }

        public int size(String firstNameFilter, String lastNameFilter) {
            List<Person> listOfPersons = new ArrayList<>(orig);
            listOfPersons = filter(listOfPersons, firstNameFilter, lastNameFilter);

            return listOfPersons.size();
        }

        private List<Person> filter(List<Person> listOfPersons, String firstNameFilter1, String lastNameFilter1) {
            if (!firstNameFilter1.isEmpty()) {
                listOfPersons = listOfPersons.stream().filter((p) -> p.getFirstName().contains(firstNameFilter1)).collect(Collectors.toList());
            }
            if (!lastNameFilter1.isEmpty()) {
                listOfPersons = listOfPersons.stream().filter((p) -> p.getLastName().contains(lastNameFilter1)).collect(Collectors.toList());
            }
            return listOfPersons;
        }

    }

}
