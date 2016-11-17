package org.vaadin.viritin;

import org.junit.Assert;
import org.junit.Test;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class SortableLazyListTest {

    @Test
    public void shouldUseNullAsSortPropertyForSortableEntityProviderWhenNoSortPropertiesAreGiven() {
        final AtomicReference<String> sortPropertyHolder = new AtomicReference<>("NOT_SET");
        SortableLazyList<Person> sortableLazyList = createList(sortPropertyHolder);
        sortableLazyList.get(0);
        Assert.assertThat(sortPropertyHolder.get(), is(nullValue()));
    }

    @Test
    public void shouldUseFirstSortPropertyWhenUsingSortableEntityProvider() {
        final AtomicReference<String> sortPropertyHolder = new AtomicReference<>("NOT_SET");
        SortableLazyList<Person> sortableLazyList = createList(sortPropertyHolder);
        sortableLazyList.setSortProperty(new String[] { "age", "firstName", "lastName" });
        sortableLazyList.get(0);
        Assert.assertThat(sortPropertyHolder.get(), is("age"));
    }

    private SortableLazyList<Person> createList(final AtomicReference<String> sortPropertyHolder) {
        return new SortableLazyList<Person>(new SortableLazyList.SortableEntityProvider<Person>() {
                @Override
                public int size() {
                    return (int) Service.count();
                }

                @Override
                public List<Person> findEntities(int firstRow, boolean sortAscending, String property) {
                    sortPropertyHolder.set(property);
                    return Service.findAll(firstRow, LazyList.DEFAULT_PAGE_SIZE);
                }
            });
    }

}
