package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.beanutils.BeanComparator;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class GridLazyLoadingEmptyResultset extends AbstractTest {

    @Override
    public Component getTestComponent() {
        final List<Person> listOfPersons = Service.getListOfPersons(0);

        MGrid<Person> g = new MGrid<>(Person.class)
                .lazyLoadFrom(
                        new SortableLazyList.SortablePagingProvider<Person>() {
                            private static final long serialVersionUID = 7430568834619612967L;

                            @Override
                    public List<Person> findEntities(int firstRow,
                            boolean sortAscending,
                            String property) {
                        if (property != null) {

                            Collections.sort(listOfPersons,
                                    new BeanComparator<>(
                                            property));
                            if (!sortAscending) {
                                Collections.reverse(listOfPersons);
                            }
                        }
                        int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
                        if (last > listOfPersons.size()) {
                            last = listOfPersons.size();
                        }
                        return new ArrayList<>(listOfPersons.subList(
                                firstRow,
                                last));
                    }
                },
                        new LazyList.CountProvider() {

                            private static final long serialVersionUID = 6199527117644735431L;

                            @Override
                    public int size() {
                        return listOfPersons.size();
                    }
                }
                );
        return g;
    }

}
