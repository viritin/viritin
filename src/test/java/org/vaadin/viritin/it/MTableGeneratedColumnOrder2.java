package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MTableGeneratedColumnOrder2 extends AbstractTest {

    @Override
    public Component getTestComponent() {

        MTable<Person> g = new MTable<>();

            g.withGeneratedColumn("age", p -> p.getAge() + " years");
            g.withProperties("age", "firstName", "lastName");
            g.withColumnHeaders("Ikä/Age", "Etu/First", "Suku/Last");

            g.lazyLoadFrom(
                    firstRow -> Service.findAll(firstRow,
                            LazyList.DEFAULT_PAGE_SIZE), () -> (int) Service.count()
            );
        return new MVerticalLayout(g);
    }

}
