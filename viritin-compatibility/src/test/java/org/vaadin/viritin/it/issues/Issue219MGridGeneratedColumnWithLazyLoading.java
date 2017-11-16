package org.vaadin.viritin.it.issues;

import com.vaadin.ui.Component;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritinv7.grid.MGrid;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
public class Issue219MGridGeneratedColumnWithLazyLoading extends AbstractTest {

    @Override
    public Component getTestComponent() {
        MGrid<Person> g = new MGrid<>(Person.class).lazyLoadFrom(
                new LazyList.PagingProvider<Person>() {
            private static final long serialVersionUID = -9072230332041322210L;

            @Override
            public List<Person> findEntities(int firstRow) {
                return Service.findAll(firstRow,
                        LazyList.DEFAULT_PAGE_SIZE);
            }
        },
                new LazyList.CountProvider() {
            private static final long serialVersionUID = -6915320247020779461L;

            @Override
            public int size() {
                return (int) Service.count();
            }
        }
        ).withGeneratedColumn("name", p -> p.getFirstName() + " " + p.getLastName())
                .withFullWidth();

        MGrid<Person> g2 = new MGrid<>(Person.class).setRows(Service.findAll(0, 100))
                .withGeneratedColumn("name", p -> p.getFirstName() + " " + p.getLastName())
                .withFullWidth();
        return new MVerticalLayout().expand(g, g2);
    }

}
