/*
 * Copyright 2017 Matti Tahvonen.
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

import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.ui.Grid;
import java.util.List;
import org.vaadin.viritin.fluency.ui.FluentAbstractComponent;

/**
 *
 * @author mstahv
 */
public class MGrid<T> extends Grid<T> implements FluentAbstractComponent<MGrid<T>> {

    /**
     * Creates an MGrid without explicit bean type. Note that certain features
     * may not work when using this constructor.
     */
    public MGrid() {
    }

    public MGrid(Class<T> beanType) {
        super(beanType);
    }
    
    protected MGrid(Class<T> beanType, DataCommunicator<T> dataCommunicator) {
        super(beanType, dataCommunicator);
    }

    protected MGrid( DataCommunicator<T> dataCommunicator) {
        super(dataCommunicator);
    }

    public MGrid<T> withProperties(String... properties) {
        setColumns(properties);
        return this;
    }

    public MGrid<T> withColumnHeaders(String... properties) {
        List<Column<T, ?>> columns = getColumns();
        for (int i = 0; i < columns.size(); i++) {
            Grid.Column<T, ? extends Object> c = columns.get(i);
            c.setCaption(properties[i]);
        }
        return this;
    }

    public MGrid<T> setRows(List<T> rows) {
        setItems(rows);
        return this;
    }

}
