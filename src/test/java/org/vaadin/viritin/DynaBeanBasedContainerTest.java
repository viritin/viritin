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
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import junit.framework.Assert;
import org.junit.Ignore;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 */
public class DynaBeanBasedContainerTest {

    Random r = new Random(0);


    final static int amount = 1000000;

    private List<Person> persons = Service.getListOfPersons(amount);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEmptyList() {
        List<Person> l = new ArrayList<>();
        // Test with BeanItemContainer
        System.out.println("BeanItemContainer with empty list");
        BeanItemContainer<Person> bc = new BeanItemContainer<>(
                Person.class, l);
        System.out.println("   container size=" + bc.size());
        System.out.print("Properties: ");
        for (String p : bc.getContainerPropertyIds()) {
            System.out.print(p + " ");
        }

        // Test ListContainer with setCollection call
        System.out.
                println("\n\nListContainer with empty list via setCollection");
        ListContainer<Person> lc = new ListContainer<>(Person.class);
        lc.setCollection(l);
        System.out.println("   container size=" + lc.size());
        System.out.print("Properties: ");
        for (String p : lc.getContainerPropertyIds()) {
            System.out.print(p + " ");
        }

        // Test ListContainer with setCollection call
        System.out.println(
                "\n\nListContainer with Class<T>, Collection<T> constructor");
        lc = new ListContainer<>(Person.class, l);
        System.out.println("   container size=" + lc.size());
        System.out.print("Properties: ");
        for (String p : lc.getContainerPropertyIds()) {
            System.out.print(p + " ");
        }
        Person per = new Person();
        per.setFirstName("First");
        per.setLastName("Lastname");
        per.setAge(r.nextInt(100));
        lc.addItem(per);
        System.out.println("\n   container size after addItem = " + lc.size());

        Person per2 = new Person();
        per2.setFirstName("Firs");
        per2.setLastName("Lastnam");
        per2.setAge(r.nextInt(100));
        l.add(per2);
        System.out.println("   container size after add = " + lc.size());

    	// Test ListContainer with constructor that takes the List -- empty List 
        // will cause zarro properties
        System.out.println(
                "\n\nListContainer with empty list via Collection<T> constructor");
        l = new ArrayList<>();
        lc = new ListContainer<>(l);
        System.out.println("   container size=" + lc.size());
        System.out.println("Properties: none should print due to exception");
        Assert.assertEquals(0, lc.getContainerPropertyIds().size());
    }

    @Test
    public void testMemoryUsage() {
        System.out.println("\n Testing List container from Maddon");

        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        ListContainer lc = new ListContainer<>(persons);
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
    @Ignore(value = "We know BeanItemContainer uses lots of memory and is slow, uncomment to verify possible fixes from core.")
    public void testMemoryUsageStd() {
        System.out.println("\n Testing BeanItemContainer from core Vaadin");

        long initial = reportMemoryUsage();

        long ms = System.currentTimeMillis();
        BeanItemContainer lc = new BeanItemContainer<>(persons);
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
    public void ensureNullFromNextAndPrevId() {
        final List<Person> persons = Service.getListOfPersons(2);

        ListContainer lc = new ListContainer<>(persons);
        
        Assert.assertNull(lc.prevItemId(persons.get(0)));
        Assert.assertEquals(persons.get(0), lc.prevItemId(persons.get(1)));
        Assert.assertEquals(persons.get(1), lc.nextItemId(persons.get(0)));
        Assert.assertNull(lc.nextItemId(persons.get(1)));

    }

}
