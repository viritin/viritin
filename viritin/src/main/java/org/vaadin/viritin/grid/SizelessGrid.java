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
package org.vaadin.viritin.grid;

import com.vaadin.server.SerializableSupplier;
import com.vaadin.ui.Grid;
import org.vaadin.viritin.fluency.ui.FluentAbstractComponent;

/**
 * Experimental Grid which doesn't require you to report the size when doing
 * lazy loading data binding.
 *
 * @author mstahv
 * @param <T> The type of data shown in the Grid
 */
public class SizelessGrid<T> extends Grid<T> implements FluentAbstractComponent<SizelessGrid<T>> {

    public SizelessGrid() {
        super(new SizelessDataCommunicator<>());
    }

    public SizelessGrid(Class<T> clazz) {
        super(clazz, new SizelessDataCommunicator<>());
    }

    public void setItems(FetchItemsCallback<T> fetchItems) {
        SerializableSupplier<Integer> sizeCallback = () -> {
            // This should never be called by the framework
            System.err.println("Size requested although should not be needed");
            return 0;
        };
        super.setDataProvider(fetchItems, sizeCallback); //To change body of generated methods, choose Tools | Templates.
    }

}
