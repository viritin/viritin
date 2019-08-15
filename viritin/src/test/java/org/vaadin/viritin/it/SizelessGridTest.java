/*
 * Copyright 2018 Matti Tahvonen.
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
package org.vaadin.viritin.it;

import java.util.List;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.grid.LazyGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Component;

/**
 *
 * @author mstahv
 */
public class SizelessGridTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        LazyGrid<Person> grid = new LazyGrid<>(Person.class);
        grid.setColumns("firstName", "lastName", "age");
        
        grid.setItems((List<QuerySortOrder> sortOrder, int offset,
                int limit) -> {

            if(sortOrder.size() > 0) {
            	System.err.println("Getting sorted data...");
            	QuerySortOrder querySortOrder = sortOrder.get(0);
            	boolean asc = querySortOrder.getDirection() == SortDirection.ASCENDING;
            	String prop = querySortOrder.getSorted();
            	return Service.findAll(offset, limit, prop, asc).stream();
            }
            return Service.findAll(offset, limit).stream();
        });
        
        
        
        return grid;
    }
    
}
