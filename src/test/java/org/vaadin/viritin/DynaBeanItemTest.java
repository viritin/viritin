package org.vaadin.viritin;

import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.testdomain.Person;
import com.vaadin.data.Item;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by evacchi on 04/12/14.
 */
public class DynaBeanItemTest {
    @Test
    public void testReturnedPropertyInstances() {
        final Person robin = new Person(0, "Dick", "Grayson", 12);
        final ListContainer<Person> listContainer =
                new ListContainer<Person>(
                        Person.class,
                        Arrays.asList(robin));

        final Item robinItem = listContainer.getItem(robin);

        assertTrue("An item instance should always the same property instance " +
                        "for a given key",
                robinItem.getItemProperty("age") == robinItem.getItemProperty("age"));

    }
}
