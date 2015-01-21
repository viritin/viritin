/*
 * Copyright 2015
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

import org.vaadin.viritin.FilterableListContainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Extension of the {@link org.vaadin.viritin.fields.MTable} class which supports filtering based on
 * {@link org.vaadin.viritin.FilterableListContainer}.
 */
public class FilterableTable<T> extends MTable<T> {

    private List<Filter> pendingFilters = new ArrayList<Filter>();

    public FilterableTable() {

    }

    public FilterableTable(Class<T> type) {
        super(type);
    }

    public FilterableTable(T... beans) {
        super(beans);
    }

    public FilterableTable(Collection<T> beans) {
        super(beans);
    }

    @Override
    protected FilterableListContainer<T> createContainer(Class<T> type) {
        return new FilterableListContainer<T>(type);
    }

    @Override
    protected FilterableListContainer<T> createContainer(Collection<T> beans) {
        return new FilterableListContainer<T>(beans);
    }

    @Override
    protected FilterableListContainer<T> getContainer() {
        return (FilterableListContainer<T>) super.getContainer();
    }

    protected void ensureBeanItemContainer(Collection<T> beans) {
        super.ensureBeanItemContainer(beans);

        for (Filter filter : pendingFilters)
            getContainer().addContainerFilter(filter);
        pendingFilters.clear();
    }

    public void addFilter(Filter filter) {
        if (isContainerInitialized()) {
            getContainer().addContainerFilter(filter);
        } else {
            pendingFilters.add(filter);
        }
    }

    public void removeFilter(Filter filter) {
        if (isContainerInitialized()) {
            getContainer().removeContainerFilter(filter);
        } else {
            pendingFilters.remove(filter);
        }
    }

    public void removeAllFilters() {
        if (isContainerInitialized()) {
            getContainer().removeAllContainerFilters();
        } else {
            pendingFilters.clear();
        }
    }

    public Collection<Filter> getFilters() {
        if (isContainerInitialized()) {
            return getContainer().getContainerFilters();
        } else {
            return Collections.unmodifiableList(pendingFilters);
        }
    }

    public FilterableTable<T> withFilter(Filter filter) {
        addFilter(filter);
        return this;
    }

    @Override
    public FilterableTable<T> addBeans(Collection<T> beans) {
        return (FilterableTable<T>) super.addBeans(beans);
    }

    @Override
    public FilterableTable<T> setBeans(T... beans) {
        return (FilterableTable<T>) super.setBeans(beans);
    }

    @Override
    public FilterableTable<T> setBeans(Collection<T> beans) {
        return (FilterableTable<T>) super.setBeans(beans);
    }

    @Override
    public FilterableTable<T> withFullWidth() {
        return (FilterableTable<T>) super.withFullWidth();
    }

    @Override
    public FilterableTable<T> withHeight(String height) {
        return (FilterableTable<T>) super.withHeight(height);
    }

    @Override
    public FilterableTable<T> withFullHeight() {
        return (FilterableTable<T>) super.withFullHeight();
    }

    @Override
    public FilterableTable<T> withWidth(String width) {
        return (FilterableTable<T>) super.withWidth(width);
    }

    @Override
    public FilterableTable<T> withCaption(String caption) {
        return (FilterableTable<T>) super.withCaption(caption);
    }

    @Override
    public FilterableTable<T> expand(String... propertiesToExpand) {
        return (FilterableTable<T>) super.expand(propertiesToExpand);
    }

    public FilterableTable<T> withGeneratedColumn(String columnId, final SimpleColumnGenerator<T> columnGenerator) {
        return (FilterableTable<T>) super.withGeneratedColumn(columnId, columnGenerator);
    }
}
