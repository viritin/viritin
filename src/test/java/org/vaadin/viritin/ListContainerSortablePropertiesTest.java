package org.vaadin.viritin;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * Tests sortable properties of ListContainer.
 */
public class ListContainerSortablePropertiesTest {

    @Test
    public void testSortableProperties() {
        ListContainer<Person> listContainer = new ListContainer<>(Service.getListOfPersons(100));
        listContainer.setContainerPropertyIds("id", "firstName", "lastName", "age", "groups[0].name", "generated");

        Object[] expectedSortableProperties = new Object[] {"id", "firstName", "lastName", "age", "groups[0].name"};
        Collection<Object> actualSortableProperties = (Collection<Object>) listContainer.getSortableContainerPropertyIds();
        Assert.assertThat(actualSortableProperties, JUnitMatchers.hasItems(expectedSortableProperties));
    }

}
