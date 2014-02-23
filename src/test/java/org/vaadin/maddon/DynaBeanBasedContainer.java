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
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;

/**
 *
 */
public class DynaBeanBasedContainer {

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

    private List<Person> persons = getListOfPersons(amount);

    @Test
    public void testMemoryUsage() {
        System.out.println("\n Testing List container from Maddon");

        long initial = reportMemoryUsage();

        Container lc = new ListContainer<Person>(persons);
        System.out.println("After creation");
        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        for (int i = 0; i < amount; i++) {
            Item item = lc.getItem(persons.get(i));
            String str;
            str = item.getItemProperty("firstName").toString();
        }

        System.out.println("After loop");
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

        Container lc = new BeanItemContainer<Person>(persons);
        System.out.println("After creation");
        long after = reportMemoryUsage();
        System.err.println("Delta (bytes)" + (after - initial));

        for (int i = 0; i < amount; i++) {
            Item item = lc.getItem(persons.get(i));
            String str;
            str = item.getItemProperty("firstName").toString();
        }

        System.out.println("After loop");
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
            Thread.sleep(1000);
            System.gc();
            Thread.sleep(1000);
            System.gc();
            Thread.sleep(1000);
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
