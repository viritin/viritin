package org.vaadin.viritin.it;

import java.util.Arrays;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.testdomain.Person;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class PerformanceBeanItemContainer extends AbstractTest {
    @Override
    public Component getTestComponent() {
        BeanItemContainer<Person> beanItemContainer = new BeanItemContainer<>(Person.class, PerformanceListContainer.LIST_OF_PERSONS);
        Table table = new Table();
        table.setContainerDataSource(beanItemContainer, Arrays.asList("firstName", "lastName"));
        return table;
    }
    
}
