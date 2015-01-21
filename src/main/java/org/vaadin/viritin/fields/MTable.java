/*
 * Copyright 2014 mattitahvonenitmill.
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
package org.vaadin.viritin.fields;

import com.vaadin.ui.Table;
import org.vaadin.viritin.ListContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * A better typed version of the Table component in Vaadin. Expects that users
 * are always listing POJOs, which is most often the case in modern Java
 * development. Uses ListContainer to bind data due to its superior performance
 * compared to BeanItemContainer.
 * <p/>
 * Note, that MTable don't support "multiselection mode.
 *
 * @param <T> the type of the POJO listed in this Table.
 */
public class MTable<T> extends Table {

    private ListContainer<T> bic;
    private String[] pendingProperties;
    private String[] pendingHeaders;

    public MTable() {
    }

    /**
     * Constructs a Table with explicit bean type. Handy for example if your
     * beans are JPA proxies or the table in empty when showing it initially.
     *
     * @param type the type of beans that are listed in this table
     */
    public MTable(Class<T> type) {
        bic = createContainer(type);
        setContainerDataSource(bic);
    }

    public MTable(T... beans) {
        this(new ArrayList<T>(Arrays.asList(beans)));
    }

    public MTable(Collection<T> beans) {
        this();
        if (beans != null) {
            bic = createContainer(beans);
            setContainerDataSource(bic);
        }
    }

    protected ListContainer<T> createContainer(Class<T> type) {
        return new ListContainer<T>(type);
    }

    protected ListContainer<T> createContainer(Collection<T> beans) {
        return new ListContainer<T>(beans);
    }

    protected ListContainer<T> getContainer() {
        return bic;
    }

    public MTable<T> withProperties(String... visibleProperties) {
        if (isContainerInitialized()) {
            setVisibleColumns((Object[]) visibleProperties);
        } else {
            pendingProperties = visibleProperties;
            for (String string : visibleProperties) {
                addContainerProperty(string, String.class, "");
            }
        }
        return this;
    }

    protected boolean isContainerInitialized() {
        return bic != null;
    }

    public MTable<T> withColumnHeaders(String... columnNamesForVisibleProperties) {
        if (isContainerInitialized()) {
            setColumnHeaders(columnNamesForVisibleProperties);
        } else {
            pendingHeaders = columnNamesForVisibleProperties;
            // Add headers to temporary indexed container, in case table is initially
            // empty
            for (String prop : columnNamesForVisibleProperties) {
                addContainerProperty(prop, String.class, "");
            }
        }
        return this;
    }

    public void addMValueChangeListener(MValueChangeListener<T> listener) {
        addListener(MValueChangeEvent.class, listener,
                MValueChangeEventImpl.VALUE_CHANGE_METHOD);
        // implicitly consider the table should be selectable
        setSelectable(true);
        // TODO get rid of this when 7.2 is out
        setImmediate(true);
    }

    public void removeMValueChangeListener(MValueChangeListener<T> listener) {
        removeListener(MValueChangeEvent.class, listener,
                MValueChangeEventImpl.VALUE_CHANGE_METHOD);
        setSelectable(hasListeners(MValueChangeEvent.class));
    }

    @Override
    protected void fireValueChange(boolean repaintIsNotNeeded) {
        super.fireValueChange(repaintIsNotNeeded);
        fireEvent(new MValueChangeEventImpl(this));
    }

    protected void ensureBeanItemContainer(Collection<T> beans) {
        if (!isContainerInitialized()) {
            bic = createContainer(beans);
            if (pendingProperties != null) {
                setContainerDataSource(bic, Arrays.asList(pendingProperties));
                pendingProperties = null;
            } else {
                setContainerDataSource(bic);
            }
            if (pendingHeaders != null) {
                setColumnHeaders(pendingHeaders);
                pendingHeaders = null;
            }
        }
    }

    @Override
    public T getValue() {
        return (T) super.getValue();
    }

    @Override
    @Deprecated
    public void setMultiSelect(boolean multiSelect) {
        super.setMultiSelect(multiSelect);
    }

    public MTable<T> addBeans(T... beans) {
        addBeans(Arrays.asList(beans));
        return this;
    }

    public MTable<T> addBeans(Collection<T> beans) {
        if (!beans.isEmpty()) {
            if (isContainerInitialized()) {
                bic.addAll(beans);
            } else {
                ensureBeanItemContainer(beans);
            }
        }
        return this;
    }

    public MTable<T> setBeans(T... beans) {
        setBeans(new ArrayList<T>(Arrays.asList(beans)));
        return this;
    }

    public MTable<T> setBeans(Collection<T> beans) {
        if (!isContainerInitialized() && !beans.isEmpty()) {
            ensureBeanItemContainer(beans);
        } else if (isContainerInitialized()) {
            bic.setCollection(beans);
        }
        return this;
    }

    public MTable<T> withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    public MTable<T> withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MTable<T> withFullHeight() {
        return withHeight("100%");
    }

    public MTable<T> withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MTable<T> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MTable<T> expand(String... propertiesToExpand) {
        for (String property : propertiesToExpand) {
            setColumnExpandRatio(property, 1);
        }
        return this;
    }

    public static interface SimpleColumnGenerator<T> {
        public Object generate(T entity);
    }

    public MTable<T> withGeneratedColumn(String columnId, final SimpleColumnGenerator<T> columnGenerator) {
        addGeneratedColumn(columnId, new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                return columnGenerator.generate((T) itemId);
            }
        });
        return this;
    }
}
