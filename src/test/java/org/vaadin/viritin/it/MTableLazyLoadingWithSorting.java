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
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MTableLazyLoadingWithSorting extends AbstractTest {

    @Override
    public Component getTestComponent() {

        final List<Person> orig = Service.getListOfPersons(1000);

        MTable<Person> g = new MTable<>(
                new SortableLazyList.SortablePagingProvider<Person>() {
            @Override
            public List<Person> findEntities(int firstRow, boolean sortAscending,
                    String property) {
                List<Person> listOfPersons = new ArrayList<>(orig);
                if (property != null) {

                    Collections.sort(listOfPersons, new BeanComparator<>(
                            property));
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
        },
                new LazyList.CountProvider() {

            @Override
            public int size() {
                return (int) Service.count();
            }
        }
        );

        Button b = new Button("Reset loading strategy (should maintain sorting)");
        b.addClickListener(e -> {
            g.lazyLoadFrom((int firstRow, boolean sortAscending, String property) -> {
                List<Person> listOfPersons = new ArrayList<>(orig);
                if (property != null) {
                    Collections.sort(listOfPersons, new BeanComparator<>(
                            property));
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
            }, () -> (int) Service.count());

        });

        return new MVerticalLayout(b, g);
    }

}
