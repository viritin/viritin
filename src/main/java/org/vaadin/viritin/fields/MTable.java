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

import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import com.vaadin.util.ReflectTools;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.SortableLazyList;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.beanutils.DynaClass;

import static org.vaadin.viritin.LazyList.DEFAULT_PAGE_SIZE;

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

    private static final long serialVersionUID = 3330985834015680723L;

    private ListContainer<T> bic;
    private String[] pendingProperties;
    private String[] pendingHeaders;

    private Collection sortableProperties;

    // Cached last sort properties, used to maintain sorting when re-setting
    // lazy load strategy
    private String sortProperty;
    private boolean sortAscending;

    public MTable() {
    }

    /**
     * Constructs a Table with explicit bean type. Handy for example if your
     * beans are JPA proxies or the table is empty when showing it initially.
     *
     * @param type the type of beans that are listed in this table
     */
    public MTable(Class<? extends T> type) {
        bic = createContainer(type);
        setContainerDataSource(bic);
    }

    public MTable(T... beans) {
        this(new ArrayList<>(Arrays.asList(beans)));
    }

    /**
     * Constructs a Table with explicit bean type. Handy for example if your
     * beans are JPA proxies or the table is empty when showing it initially.
     *
     * @param type the type of beans that are listed in this table
     */
    public MTable(DynaClass type) {
        bic = createContainer(type);
        setContainerDataSource(bic);
    }

    /**
     * A shorthand to create MTable using LazyList. By default page size of
     * LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MTable(LazyList.PagingProvider<T> pageProvider,
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
    public MTable(LazyList.PagingProvider<T> pageProvider,
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
    public MTable(SortableLazyList.SortablePagingProvider<T> pageProvider,
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
    public MTable(SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(new SortableLazyList(pageProvider, countProvider, pageSize));
    }

    /**
     * A shorthand to create MTable using LazyList. By default page size of
     * LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param rowType the type of entities listed in the table
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MTable(Class<T> rowType, LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        this(rowType, pageProvider, countProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * A shorthand to create MTable using LazyList.
     *
     * @param rowType the type of entities listed in the table
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     */
    public MTable(Class<T> rowType, LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(rowType);
        lazyLoadFrom(pageProvider, countProvider, pageSize);
    }

    /**
     * A shorthand to create MTable using SortableLazyList. By default page size
     * of LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param rowType the type of entities listed in the table
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MTable(Class<T> rowType,
            SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        this(rowType, pageProvider, countProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * A shorthand to create MTable using SortableLazyList.
     *
     * @param rowType the type of entities listed in the table
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     */
    public MTable(Class<T> rowType,
            SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(rowType);
        lazyLoadFrom(pageProvider, countProvider, pageSize);
    }

    public MTable(Collection<T> beans) {
        this();
        if (beans != null) {
            bic = createContainer(beans);
            setContainerDataSource(bic);
        }
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @return this MTable object
     */
    public MTable<T> lazyLoadFrom(LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        setBeans(new LazyList(pageProvider, countProvider, DEFAULT_PAGE_SIZE));
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     * @return this MTable object
     */
    public MTable<T> lazyLoadFrom(LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        setBeans(new LazyList(pageProvider, countProvider, pageSize));
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @return this MTable object
     */
    public MTable<T> lazyLoadFrom(
            SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        setBeans(new SortableLazyList(pageProvider, countProvider,
                DEFAULT_PAGE_SIZE));
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     * @return this MTable object
     */
    public MTable<T> lazyLoadFrom(
            SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        setBeans(new SortableLazyList(pageProvider, countProvider, pageSize));
        return this;
    }

    protected ListContainer<T> createContainer(Class<? extends T> type) {
        return new ListContainer<T>(type); // Type parameter just to keep NB happy
    }

    private ListContainer<T> createContainer(DynaClass type) {
        return new ListContainer<>(type);
    }

    protected ListContainer<T> createContainer(Collection<T> beans) {
        return new ListContainer<>(beans);
    }

    protected ListContainer<T> getContainer() {
        return bic;
    }

    public MTable<T> withProperties(String... visibleProperties) {
        if (isContainerInitialized()) {
            bic.setContainerPropertyIds(visibleProperties);
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
            for (int i = 1; i < parts.length; i++) {
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
     * the propertyId has to been added before!
     *
     * @param propertyId columns property id
     * @param width width to be reserved for columns content
     * @return MTable
     */
    public MTable<T> withColumnWidth(String propertyId, int width) {
        setColumnWidth(propertyId, width);
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
                bic.setContainerPropertyIds(pendingProperties);
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
        setBeans(new ArrayList<>(Arrays.asList(beans)));
        return this;
    }

    public MTable<T> setRows(T... beansForRows) {
        return setBeans(beansForRows);
    }

    public MTable<T> setBeans(Collection<T> beans) {

        if (sortProperty != null && beans instanceof SortableLazyList) {
            final SortableLazyList sll = (SortableLazyList) beans;
            sll.setSortProperty(new String[]{sortProperty});
            sll.setSortAscending(new boolean[]{sortAscending});
        }

        if (!isContainerInitialized() && !beans.isEmpty()) {
            ensureBeanItemContainer(beans);
        } else if (isContainerInitialized()) {
            bic.setCollection(beans);
        }
        return this;
    }

    public MTable<T> setRows(Collection<T> beansForRows) {
        return setBeans(beansForRows);
    }

    /**
     * Makes the first column of the table a primary column, for which all space
     * left out from other columns is given. The method also makes sure the
     * Table has a width defined (otherwise the setting makes no sense).
     *
     *
     * @return {@link MTable}
     */
    public MTable<T> expandFirstColumn() {
        expand(getContainerPropertyIds().iterator().next().toString());
        if (getWidth() == -1) {
            return withFullWidth();
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

    public MTable<T> withSize(MSize mSize) {
        setWidth(mSize.getWidth(), mSize.getWidthUnit());
        setHeight(mSize.getHeight(), mSize.getHeightUnit());
        return this;
    }

    public MTable<T> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MTable<T> withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MTable<T> withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MTable<T> withId(String id) {
        setId(id);
        return this;
    }

    public MTable<T> expand(String... propertiesToExpand) {
        for (String property : propertiesToExpand) {
            setColumnExpandRatio(property, 1);
        }
        return this;
    }

    /**
     * the propertyId has to been added before!
     *
     * @param propertyId columns property id
     * @param ratio the expandRatio used to divide excess space for this column
     * @return MTable
     */
    public MTable<T> withColumnExpand(String propertyId, float ratio) {
        setColumnExpandRatio(propertyId, ratio);
        return this;
    }

    private ItemClickListener itemClickPiggyback;

    private void ensureTypedItemClickPiggybackListener() {
        if (itemClickPiggyback == null) {
            itemClickPiggyback = new ItemClickListener() {
                private static final long serialVersionUID = -2318797984292753676L;

                @Override
                public void itemClick(ItemClickEvent event) {
                    fireEvent(new RowClickEvent<T>(event));
                }
            };
            addItemClickListener(itemClickPiggyback);
        }
    }

    public MTable<T> withProperties(List<String> a) {
        return withProperties(a.toArray(new String[a.size()]));
    }

    public static interface SimpleColumnGenerator<T> {

        public Object generate(T entity);
    }

    public MTable<T> withGeneratedColumn(String columnId,
            final SimpleColumnGenerator<T> columnGenerator) {
        addGeneratedColumn(columnId, new ColumnGenerator() {
            private static final long serialVersionUID = 2855441121974230973L;

            @Override
            public Object generateCell(Table source, Object itemId,
                    Object columnId) {
                return columnGenerator.generate((T) itemId);
            }
        });
        return this;
    }

    public static class SortEvent extends Component.Event {

        private static final long serialVersionUID = 267382182533317834L;

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
            sortAscending = ascending != null && ascending.length > 0 ? ascending[0] : true;
            sortProperty = propertyId != null && propertyId.length > 0 ? propertyId[0].
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
        } catch (UnsupportedOperationException e) {
            throw new RuntimeException(e);
        } finally {
            isSorting = false;
            if (refreshingPreviouslyEnabled) {
                enableContentRefreshing(true);
            }
        }
    }

    /**
     * A version of ItemClickEvent that is properly typed and named.
     *
     * @param <T> the type of the row
     */
    public static class RowClickEvent<T> extends MouseEvents.ClickEvent {

        private static final long serialVersionUID = -73902815731458960L;

        public static final Method TYPED_ITEM_CLICK_METHOD;

        static {
            try {
                TYPED_ITEM_CLICK_METHOD = RowClickListener.class.
                        getDeclaredMethod("rowClick",
                                new Class[]{RowClickEvent.class});
            } catch (final java.lang.NoSuchMethodException e) {
                // This should never happen
                throw new java.lang.RuntimeException();
            }
        }

        private final ItemClickEvent orig;

        public RowClickEvent(ItemClickEvent orig) {
            super(orig.getComponent(), null);
            this.orig = orig;
        }

        /**
         * @return the entity(~row) that was clicked.
         */
        public T getEntity() {
            return (T) orig.getItemId();
        }

        /**
         * @return the entity(~row) that was clicked.
         */
        public T getRow() {
            return getEntity();
        }

        /**
         * @return the identifier of the column on which the row click happened.
         */
        public String getColumnId() {
            return orig.getPropertyId().toString();
        }

        @Override
        public MouseEventDetails.MouseButton getButton() {
            return orig.getButton();
        }

        @Override
        public int getClientX() {
            return orig.getClientX();
        }

        @Override
        public int getClientY() {
            return orig.getClientY();
        }

        @Override
        public int getRelativeX() {
            return orig.getRelativeX();
        }

        @Override
        public int getRelativeY() {
            return orig.getRelativeY();
        }

        @Override
        public boolean isAltKey() {
            return orig.isAltKey();
        }

        @Override
        public boolean isCtrlKey() {
            return orig.isCtrlKey();
        }

        @Override
        public boolean isDoubleClick() {
            return orig.isDoubleClick();
        }

        @Override
        public boolean isMetaKey() {
            return orig.isMetaKey();
        }

        @Override
        public boolean isShiftKey() {
            return orig.isShiftKey();
        }

    }

    /**
     * A better typed version of ItemClickEvent.
     *
     * @param <T> the type of entities listed in the table
     */
    public interface RowClickListener<T> extends Serializable {

        public void rowClick(RowClickEvent<T> event);
    }

    public void addRowClickListener(RowClickListener<T> listener) {
        ensureTypedItemClickPiggybackListener();
        addListener(RowClickEvent.class, listener,
                RowClickEvent.TYPED_ITEM_CLICK_METHOD);
    }

    public void removeRowClickListener(RowClickListener<T> listener) {
        removeListener(RowClickEvent.class, listener,
                RowClickEvent.TYPED_ITEM_CLICK_METHOD);
    }

    /**
     * Clears caches in case the Table is backed by a LazyList implementation.
     * Also resets "pageBuffer" used by table. If you know you have changes in
     * the listing, you can call this method to ensure the UI gets updated.
     *
     * @deprecated use refreshRows instead
     */
    @Deprecated
    public void resetLazyList() {
        refreshRows();
    }

    /**
     * Clears caches in case the Table is backed by a LazyList implementation.
     * Also resets "pageBuffer" used by table. If you know you have changes in
     * the listing, you can call this method to ensure the UI gets updated.
     */
    public void refreshRows() {
        if (bic != null && bic.getItemIds() instanceof LazyList) {
            ((LazyList) bic.getItemIds()).reset();
        }
        resetPageBuffer();
    }

    /**
     * Sets the row of given entity as selected. This is practically a better
     * typed version for select(Object) and setValue(Object) methods.
     *
     * @param entity the entity whose row should be selected
     * @return the MTable instance
     */
    public MTable<T> setSelected(T entity) {
        setValue(entity);
        return this;
    }

    public MTable<T> withValueChangeListener(MValueChangeListener<T> listener){
        addMValueChangeListener(listener);
        return this;
    }

    public MTable<T> withRowClickListener(RowClickListener<T> listener){
        addRowClickListener(listener);
        return this;
    }

    public MTable<T> withSortListener(SortListener listener){
        addSortListener(listener);
        return this;
    }
}
