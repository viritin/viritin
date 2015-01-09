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

import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.testdomain.Person;
import java.util.ArrayList;
import org.junit.Test;

/**
 *
 * @author Matti Tahvonen <matti@vaadin.com>
 */
public class NullEntryComparator {
    
    public NullEntryComparator() {
    }
    
    @Test
    public void test() {
        ArrayList<Person> arrayList = new ArrayList<Person>();
        Person person = new Person();
        person.setFirstName("Matti");
        arrayList.add(person);
        person = new Person();
        person.setFirstName(null);
        arrayList.add(person);
        ListContainer<Person> listContainer = new ListContainer<Person>(arrayList);
        listContainer.sort(new Object[]{"firstName"}, new boolean[]{false});
        
    }

}
