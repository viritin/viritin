/*
 * Copyright 2014 Matti Tahvonen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.viritin;

import org.vaadin.viritin.testdomain.Person;
import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.SimpleStringFilter;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import junit.framework.Assert;
import org.apache.commons.lang3.mutable.MutableBoolean;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Ignore;
import static org.junit.matchers.JUnitMatchers.hasItems;

/**
 *
 */
public class FilterableListContainerTest {

    Random r = new Random(0);

    private List<Person> getListOfPersons(int total) {
        List<Person> l = new ArrayList<>(total);
        for (int i = 0; i < total; i++) {
            Person p = new Person();
            p.setId(i);
            p.setFirstName("First" + i);
            p.setLastName("Lastname" + i);
            p.setAge(r.nextInt(100));
            l.add(p);
        }
        return l;
    }

    final static int amount = 1000000;
    //final static int amount = 100000;

    private final Filter ageFilter = new Between("age", 30, 40);

    private final List<Person> persons = getListOfPersons(amount);

    @Test
    public void clearFilters() {
        final List<Person> listOfPersons = getListOfPersons(100);
        FilterableListContainer<Person> container = new FilterableListContainer<>(
                listOfPersons);
        container.addContainerFilter(new SimpleStringFilter("firstName",
                "First1", true, true));
        Assert.assertNotSame(listOfPersons.size(), container.size());
        container.removeAllContainerFilters();
        Assert.assertEquals(listOfPersons.size(), container.size());
        container.addContainerFilter(new SimpleStringFilter("firstName",
                "foobar", true, true));
        Assert.assertEquals(0, container.size());

        final MutableBoolean fired = new MutableBoolean(false);
        container.addListener(new Container.ItemSetChangeListener() {
            @Override
            public void containerItemSetChange(
                    Container.ItemSetChangeEvent event) {
                fired.setTrue();
            }
        });
        container.removeAllContainerFilters();
        Assert.assertTrue(fired.booleanValue());
        Assert.assertEquals(listOfPersons.size(), container.size());
    }
    
    @Test
    public void testFilterableListContainerPerformance() {
        System.out.println(
                "\n Testing FilterableListContainer from Viritin (with Filter)");

        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        FilterableListContainer<Person> lc = new FilterableListContainer<>(
                persons);
        System.out.println(
                "After creation with " + amount + " beans (took " + (System.
                currentTimeMillis() - ms) + ")");

        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        doTests(lc, initial);

    }

    @Test
    @Ignore(value = "we know BeanItemContainer filtering is veeery slow, re-activate to investigate possible enhancements in the core.")
    public void testFilterStd() {
        System.out.println(
                "\n Testing BeanItemContainer from core Vaadin (with Filter)");
        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        BeanItemContainer<Person> lc = new BeanItemContainer<>(
                Person.class, persons);
        System.out.println(
                "After creation with " + amount + " beans (took " + (System.
                currentTimeMillis() - ms) + ")");

        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        doTests(lc, initial);

    }

    @Test
    public void testSortingWhenFiltered() {
        FilterableListContainer<Person> lc = new FilterableListContainer<>(
                persons);
        lc.addContainerFilter(new SimpleStringFilter("firstName",
                "First10000", true, true));
        lc.sort(new Object[]{"firstName"}, new boolean[]{false});
        Person p = lc.getIdByIndex(0);
        Assert.assertEquals("First100009", p.getFirstName());
        Assert.assertEquals("First10000", lc.getIdByIndex(10).getFirstName());
        lc.sort(new Object[]{"firstName"}, new boolean[]{true});
        Assert.assertEquals("First10000", lc.getIdByIndex(0).getFirstName());
        Assert.assertEquals("First100009", lc.getIdByIndex(10).getFirstName());
    }

    private void doTests(Container.Filterable lc, long initial) {
        long ms = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            Item item = lc.getItem(persons.get(i));
            String str;
            str = item.getItemProperty("firstName").toString();
        }

        System.out.println(
                "After loop (took " + (System.currentTimeMillis() - ms) + ")");
        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        ms = System.currentTimeMillis();
        lc.addContainerFilter(ageFilter);
        int fSize = lc.size();
        System.out.println(
                "Time to filter " + fSize + " out of " + amount + " elements: " + (System.
                currentTimeMillis() - ms));
        ms = System.currentTimeMillis();
        for (Object o : lc.getItemIds()) {
            String str;
            Person bean = (Person) o;
            str = bean.getFirstName();
        }
        System.out.println(
                "After loop through filtered list (took " + (System.
                currentTimeMillis() - ms) + ")");
        long afterF = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (afterF - after));

        // call to avoid GC:n the whole container
        lc.getItemIds();
        System.out.println("After GC");
        after = reportMemoryUsage();
        System.out.println();
        System.err.println("Delta (bytes)" + (after - initial));
    }

    private long reportMemoryUsage() {
        try {
            System.gc();
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
            System.gc();
            Thread.sleep(100);
            System.gc();
        } catch (InterruptedException ex) {
        }
        MemoryUsage mu = ManagementFactory.getMemoryMXBean().
                getHeapMemoryUsage();
        System.out.println("Memory used (M):" + mu.getUsed() / 1000000);
        return ManagementFactory.getMemoryMXBean().
                getHeapMemoryUsage().getUsed();
    }

   @Test
   public void testFirstLast() {
       FilterableListContainer<Person> lc = new FilterableListContainer<>(
               new ArrayList<>(Arrays.asList(
                   new Person(0, "1", "1", 1),
                   new Person(0, "2", "2", 2),
                   new Person(0, "3", "3", 3)
       )));

       lc.addContainerFilter(new Compare.Greater("age", 1));
       assertNotSame(1, lc.firstItemId().getAge());
       lc.addContainerFilter(new Compare.LessOrEqual("age", 1));
       try {
           assertNull(lc.firstItemId());
           assertNull(lc.lastItemId());
       } catch (ArrayIndexOutOfBoundsException ex) {
           fail("Exception was thrown: " + ex);
       }
   }

  @Test
  public void testMultiLevelSort() throws Exception {
    FilterableListContainer<Person> lc = new FilterableListContainer<>(
          new ArrayList<>(Arrays.asList(
                new Person(0, "2", "2", 3),
                new Person(0, "3", "2", 2),
                new Person(0, "1", "2", 2),
                new Person(0, "1", "1", 1),
                new Person(0, "1", "2", 4)
          )));

    lc.sort(new Object[]{"age", "firstName"}, new boolean[]{true, false});

    Collection<Person> itemIds = lc.getItemIds();
    assertThat(itemIds, hasItems(
          new Person(0, "1", "1", 1),
          new Person(0, "3", "2", 2),
          new Person(0, "1", "2", 2),
          new Person(0, "2", "2", 3),
          new Person(0, "1", "2", 4)
    ));
  }
}
