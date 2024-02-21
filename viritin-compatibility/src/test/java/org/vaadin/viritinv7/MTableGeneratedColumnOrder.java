package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import java.util.List;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.it.AbstractTest;
import org.vaadin.viritinv7.fields.MTable;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MTableGeneratedColumnOrder extends AbstractTest {

    @Override
    public Component getTestComponent() {

        MTable<Person> g = new MTable<>(
                Person.class)
                .withGeneratedColumn("age", p -> p.getAge() + " years")
                .withProperties("age", "firstName", "lastName")
                .withColumnHeaders("Ikä/Age", "Etu/First", "Suku/Last")
                .lazyLoadFrom(
                        firstRow -> Service.findAll(firstRow,
                        LazyList.DEFAULT_PAGE_SIZE), () -> (int) Service.count()
                );
        return g;
    }

}
