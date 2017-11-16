package org.vaadin.viritinv7;

import com.vaadin.ui.Component;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritinv7.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
//@Theme("valo")
public class GridLazyLoading extends AbstractTest {

    private static final long serialVersionUID = 7741969263616707523L;

    @Override
    public Component getTestComponent() {

        MGrid<Person> g = new MGrid<>(
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
        );
        
        return g;
    }
}