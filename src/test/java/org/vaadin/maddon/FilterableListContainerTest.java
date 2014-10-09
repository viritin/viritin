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
package org.vaadin.maddon;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Between;
import com.vaadin.data.util.filter.SimpleStringFilter;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import junit.framework.Assert;
import org.apache.commons.lang3.mutable.MutableBoolean;

import org.junit.Test;

/**
 *
 */
public class FilterableListContainerTest {

    Random r = new Random(0);

    private List<Person> getListOfPersons(int total) {
        List<Person> l = new ArrayList<Person>(total);
        for (int i = 0; i < total; i++) {
            Person p = new Person();
            p.setFirstName("Fist" + i);
            p.setLastName("Lastname" + i);
            p.setAge(r.nextInt(100));
            l.add(p);
        }
        return l;
    }

    final static int amount = 1000000;
    //final static int amount = 100000;
    
    private Filter ageFilter = new Between("age", 30, 40);
    

    private List<Person> persons = getListOfPersons(amount);
    
    @Test
    public void clearFilters() {
        final List<Person> listOfPersons = getListOfPersons(100);
        FilterableListContainer<Person> container = new FilterableListContainer<Person>(listOfPersons);
        container.addContainerFilter(new SimpleStringFilter("firstName", "First1",true, true));
        Assert.assertNotSame(listOfPersons.size(), container.size());
        container.removeAllContainerFilters();
        Assert.assertEquals(listOfPersons.size(), container.size());
        container.addContainerFilter(new SimpleStringFilter("firstName", "foobar",true, true));
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
    public void testMemoryUsage() {
        System.out.println("\n Testing Filterable List container from Maddon");

        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        ListContainer<Person> lc = new ListContainer<Person>(persons);
        System.out.println(
                "After creation (took " + (System.currentTimeMillis() - ms) + ")");
        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        ms = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            Item item = lc.getItem(persons.get(i));
            String str;
            str = item.getItemProperty("firstName").toString();
        }

        System.out.println(
                "After loop (took " + (System.currentTimeMillis() - ms) + ")");
        after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        // call to avoid GC:n the whole container
        lc.getItemIds();
        System.out.println("After GC");
        after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

    }

    @Test
    public void testMemoryUsageStd() {
        System.out.println("\n Testing BeanItemContainer from core Vaadin");

        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        BeanItemContainer<Person> lc = new BeanItemContainer<Person>(Person.class, persons);
        System.out.println(
                "After creation (took " + (System.currentTimeMillis() - ms) + ")");
        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        ms = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            Item item = lc.getItem(persons.get(i));
            String str;
            str = item.getItemProperty("firstName").toString();
        }

        System.out.println(
                "After loop (took " + (System.currentTimeMillis() - ms) + ")");
        after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        // call to avoid GC:n the whole container
        lc.getItemIds();

        System.out.println("After GC");
        after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

    }

    @Test
    public void testFilterMaddon() {
        System.out.println("\n Testing FilterableListContainer from Maddon (with Filter)");

        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        FilterableListContainer<Person> lc = new FilterableListContainer<Person>(persons);
        System.out.println(
                "After creation with " + amount + " beans (took " + (System.currentTimeMillis() - ms) + ")");
        
        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        doTests(lc, initial);
    	
    }
    
    @Test
    public void testFilterStd() {
        System.out.println("\n Testing BeanItemContainer from core Vaadin (with Filter)");
        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        BeanItemContainer<Person> lc = new BeanItemContainer<Person>(Person.class, persons);
        System.out.println(
                "After creation with " + amount + " beans (took " + (System.currentTimeMillis() - ms) + ")");

        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        doTests(lc, initial);
        
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
        System.out.println("Time to filter " + fSize + " out of " + amount + " elements: " + (System.currentTimeMillis() - ms));
        ms = System.currentTimeMillis();
        for (Object o : lc.getItemIds()) {
        	String str;
        	Person bean = (Person) o;
        	str = bean.getFirstName();
        }
        System.out.println(
                "After loop through filtered list (took " + (System.currentTimeMillis() - ms) + ")");
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

}
