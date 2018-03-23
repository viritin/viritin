package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.beanutils.BeanComparator;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class GridLazyLoadingAndSorting extends AbstractTest {
    
    ServiceLayerExample service = new ServiceLayerExample();

    @Override
    public Component getTestComponent() {

        final MGrid<Person> g = new MGrid<>(
                new SortableLazyList.SortablePagingProvider<Person>() {
            private static final long serialVersionUID = 8990276045925275684L;

            @Override
            public List<Person> findEntities(int firstRow, boolean sortAscending,
                    String sortProperty) {
                return service.findPersons(sortProperty, sortAscending, firstRow, LazyList.DEFAULT_PAGE_SIZE);
            }
        },
                new LazyList.CountProvider() {

            private static final long serialVersionUID = 6575441260380762210L;

            @Override
            public int size() {
                return service.size();
            }
        }
        );

        Button b = new Button("Reset loading strategy (should maintaine sort order)");
        b.addClickListener(e -> {
            g.lazyLoadFrom((int firstRow, boolean sortAscending, String property) -> {
                return service.findPersons(property, sortAscending, firstRow, LazyList.DEFAULT_PAGE_SIZE);
            }, () -> service.size());

        });

        return new MVerticalLayout(b, g);
    }

    /**
     * An example service layer. The data is here just in memory, which we'll 
     * sort/filter in the methods, but in real world case you'd of course make 
     * e.g. DB queries based on the parameters.
     */
    public static class ServiceLayerExample {

        final List<Person> orig = Service.getListOfPersons(1000);

        public List<Person> findPersons(String sortProperty, boolean sortAscending, int firstRow, int DEFAULT_PAGE_SIZE) {
            List<Person> listOfPersons = new ArrayList<>(orig);
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
        
        public int size() {
            return orig.size();
        }

    }

}
