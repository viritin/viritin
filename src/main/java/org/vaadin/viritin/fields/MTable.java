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

import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.util.ReflectTools;
import java.lang.reflect.Method;
import org.vaadin.viritin.ListContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.LazyList;
import static org.vaadin.viritin.LazyList.DEFAULT_PAGE_SIZE;
import org.vaadin.viritin.SortableLazyList;

/**
 * A better typed version of the Table component in Vaadin. Expects that users
 * are always listing POJOs, which is most often the case in modern Java
 * development. Uses ListContainer to bind data due to its superior performance
 * compared to BeanItemContainer.
 * <p>
 * Note, that MTable don't support "multiselection mode". It is also very little
 * tested in "editable mode".
 * <p>
 * If your "list" of entities is too large to load into memory, there are also
 * constructors for typical "service layers". Then paged requests are used to
 * fetch entities that are visible (or almost visible) in the UI. Behind the
 * scenes LazyList is used to "wrap" your service into list.
 *
 * @param <T> the type of the POJO listed in this Table.
 */
public class MTable<T> extends Table {

    private ListContainer<T> bic;
    private String[] pendingProperties;
    private String[] pendingHeaders;

    private Collection sortableProperties;

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

    /**
     * A shorthand to create MTable using LazyList. By default page size of
     * LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MTable(LazyList.PagingProvider pageProvider,
            LazyList.CountProvider countProvider) {
        this(new LazyList(pageProvider, countProvider, DEFAULT_PAGE_SIZE));
    }

    /**
     * A shorthand to create MTable using LazyList.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     */
    public MTable(LazyList.PagingProvider pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(new LazyList(pageProvider, countProvider, pageSize));
    }

    /**
     * A shorthand to create MTable using SortableLazyList. By default page size
     * of LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MTable(SortableLazyList.SortablePagingProvider pageProvider,
            LazyList.CountProvider countProvider) {
        this(new SortableLazyList(pageProvider, countProvider, DEFAULT_PAGE_SIZE));
    }

    /**
     * A shorthand to create MTable using SortableLazyList.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     */
    public MTable(SortableLazyList.SortablePagingProvider pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(new SortableLazyList(pageProvider, countProvider, pageSize));
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
        for (String visibleProperty : visibleProperties) {
            String[] parts = StringUtils.splitByCharacterTypeCamelCase(
                    visibleProperty);
            parts[0] = StringUtils.capitalize(parts[0]);
            for(int i = 1; i < parts.length; i++) {
                parts[i] = parts[i].toLowerCase();
            }
            String saneCaption = StringUtils.join(parts, " ");
            setColumnHeader(visibleProperty, saneCaption);
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

    /**
     * Explicitly sets which properties are sortable in the UI.
     *
     * @param sortableProperties the collection of property identifiers/names
     * that should be sortable
     * @return the MTable instance
     */
    public MTable<T> setSortableProperties(Collection sortableProperties) {
        this.sortableProperties = sortableProperties;
        return this;
    }

    /**
     * Explicitly sets which properties are sortable in the UI.
     *
     * @param sortableProperties the collection of property identifiers/names
     * that should be sortable
     * @return the MTable instance
     */
    public MTable<T> setSortableProperties(String... sortableProperties) {
        this.sortableProperties = Arrays.asList(sortableProperties);
        return this;
    }

    public Collection getSortableProperties() {
        return sortableProperties;
    }

    @Override
    public Collection<?> getSortableContainerPropertyIds() {
        if (getSortableProperties() != null) {
            return Collections.unmodifiableCollection(sortableProperties);
        }
        return super.getSortableContainerPropertyIds();
    }

    public void addMValueChangeListener(MValueChangeListener<T> listener) {
        addListener(MValueChangeEvent.class, listener,
                MValueChangeEventImpl.VALUE_CHANGE_METHOD);
        // implicitly consider the table should be selectable
        setSelectable(true);
	// Needed as client side checks only for "real value change listener"
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

    public MTable<T> withGeneratedColumn(String columnId,
            final SimpleColumnGenerator<T> columnGenerator) {
        addGeneratedColumn(columnId, new ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                return columnGenerator.generate((T) itemId);
            }
        });
        return this;
    }

    public static class SortEvent extends Component.Event {

        private boolean preventContainerSort = false;
        private final boolean sortAscending;
        private final String sortProperty;

        public SortEvent(Component source, boolean sortAscending,
                String property) {
            super(source);
            this.sortAscending = sortAscending;
            this.sortProperty = property;
        }

        public String getSortProperty() {
            return sortProperty;
        }

        public boolean isSortAscending() {
            return sortAscending;
        }

        /**
         * By calling this method you can prevent the sort call to the container
         * used by MTable. In this case you most most probably you want to
         * manually sort the container instead.
         */
        public void preventContainerSort() {
            preventContainerSort = true;
        }

        public boolean isPreventContainerSort() {
            return preventContainerSort;
        }

        private final static Method method = ReflectTools.findMethod(
                SortListener.class, "onSort",
                SortEvent.class);

    }

    /**
     * A listener that can be used to track when user sorts table on a column.
     *
     * Via the event user can also prevent the "container sort" done by the
     * Table and implement own sorting logic instead (e.g. get a sorted list of
     * entities from the backend).
     *
     */
    public interface SortListener {

        public void onSort(SortEvent event);

    }

    public MTable addSortListener(SortListener listener) {
        addListener(SortEvent.class, listener, SortEvent.method);
        return this;
    }

    public MTable removeSortListener(SortListener listener) {
        removeListener(SortEvent.class, listener, SortEvent.method);
        return this;
    }

    private boolean isSorting = false;

    @Override
    public void sort(Object[] propertyId, boolean[] ascending) throws UnsupportedOperationException {
        if (isSorting) {
            // hack to avoid recursion
            return;
        }

        boolean refreshingPreviouslyEnabled = disableContentRefreshing();
        boolean defaultTableSortingMethod = false;
        try {
            isSorting = true;

            // create sort event and fire it, allow user to prevent default
            // operation
            final boolean sortAscending = ascending != null && ascending.length > 0 ? ascending[0] : true;
            final String sortProperty = propertyId != null && propertyId.length > 0 ? propertyId[0].
                    toString() : null;

            final SortEvent sortEvent = new SortEvent(this, sortAscending,
                    sortProperty);
            fireEvent(sortEvent);

            if (!sortEvent.isPreventContainerSort()) {
                // if not prevented, do sorting
                if (bic != null && bic.getItemIds() instanceof SortableLazyList) {
                    // Explicit support for SortableLazyList, set sort parameters
                    // it uses to backend services and clear internal buffers
                    SortableLazyList<T> sll = (SortableLazyList) bic.
                            getItemIds();
                    if (ascending == null || ascending.length == 0) {
                        sll.sort(true, null);
                    } else {
                        sll.sort(ascending[0], propertyId[0].toString());
                    }
                    resetPageBuffer();
                } else {
                    super.sort(propertyId, ascending);
                    defaultTableSortingMethod = true;
                }
            }
            if (!defaultTableSortingMethod) {
                // Ensure the values used in UI are set as this method is public
                // and can be called by both UI event and app logic
                setSortAscending(sortAscending);
                setSortContainerPropertyId(sortProperty);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            isSorting = false;
            if (refreshingPreviouslyEnabled) {
                enableContentRefreshing(true);
            }
        }
    }

    /**
     * Clears caches in case the Table is backed by a LazyList implementation.
     * Also resets "pageBuffer" used by table. If you know you have changes in
     * the listing, you can call this method to ensure the UI gets updated.
     */
    public void resetLazyList() {
        if (bic != null && bic.getItemIds() instanceof LazyList) {
            ((LazyList) bic.getItemIds()).reset();
        }
        resetPageBuffer();
    }

}
