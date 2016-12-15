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
import org.vaadin.viritin.testdomain.Service;

import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class ListContainerTest {

    @Test
    public void testSort() {
        ListContainer<Person> lc = new ListContainer<>(Service.getListOfPersons(100));
        lc.sort(new Object[] {"age","firstName"}, new boolean[] {true, false});
        List<Person> sortedList = lc.getBackingList();

        for (int i=0; i<sortedList.size()-1; i++) {
            Person a = sortedList.get(i);
            Person b = sortedList.get(i+1);
            int ageCompare = a.getAge().compareTo(b.getAge());
            assertTrue(ageCompare <= 0);
            if (ageCompare == 0) {
                int nameCompare = a.getFirstName().compareTo(b.getFirstName());
                assertTrue(nameCompare >= 0);
            }
        }
    }

}
