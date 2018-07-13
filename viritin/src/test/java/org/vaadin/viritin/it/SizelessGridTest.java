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

import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.ui.Component;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.grid.SizelessGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author mstahv
 */
public class SizelessGridTest extends AbstractTest {

    @Override
    public Component getTestComponent() {
        SizelessGrid<Person> grid = new SizelessGrid<>(Person.class);
        
        grid.setItems((List<QuerySortOrder> sortOrder, int offset,
                int limit) -> {
            return Service.findAll(offset, limit).stream();
        });
        
        return grid;
    }
    
}
