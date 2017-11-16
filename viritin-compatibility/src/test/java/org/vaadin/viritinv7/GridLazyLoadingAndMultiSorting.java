package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.beanutils.BeanComparator;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritinv7.SortableLazyList;
import org.vaadin.viritinv7.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class GridLazyLoadingAndMultiSorting extends AbstractTest {

    @Override
    public Component getTestComponent() {

        final List<Person> orig = Service.getListOfPersons(1000);

        final MGrid<Person> g = new MGrid<>(
                new SortableLazyList.MultiSortablePagingProvider<Person>() {
            private static final long serialVersionUID = 8990276045925275684L;

            @Override
            public List<Person> findEntities(int firstRow, boolean[] sortAscending, String[] property) {
                List<Person> listOfPersons = new ArrayList<>(orig);
                if (property != null) {
                    for (int i = property.length - 1 ; i >= 0; i--) {
                        String s = property[i];
                        boolean a = sortAscending[i];
                        Collections.sort(listOfPersons, new BeanComparator<>(
                                s));
                        if (!a) {
                            Collections.reverse(listOfPersons);
                        }
                    }
                }
                int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
                if (last > listOfPersons.size()) {
                    last = listOfPersons.size();
                }
                return new ArrayList<>(listOfPersons.subList(firstRow,
                        last));

            }
        },
                new LazyList.CountProvider() {

            private static final long serialVersionUID = 6575441260380762210L;

            @Override
            public int size() {
                return orig.size();
            }
        }
        );

        Button b = new Button("Reset loading strategy (should maintaine sort order)");
        b.addClickListener(e -> {
            g.lazyLoadFrom(
                    (int firstRow, boolean[] sortAscending, String[] property) -> {
                        List<Person> listOfPersons = new ArrayList<>(orig);
                        if (property != null) {
                            for (int i = property.length - 1 ; i >= 0; i--) {
                                String s = property[i];
                                boolean a = sortAscending[i];
                                Collections.sort(listOfPersons, new BeanComparator<>(
                                        s));
                                if (!a) {
                                    Collections.reverse(listOfPersons);
                                }
                            }
                        }
                        int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
                        if (last > listOfPersons.size()) {
                            last = listOfPersons.size();
                        }
                        return new ArrayList<>(listOfPersons.subList(firstRow,
                                last));
                    },
                    () -> orig.size());
        });

        return new MVerticalLayout(b, g);
    }

}
