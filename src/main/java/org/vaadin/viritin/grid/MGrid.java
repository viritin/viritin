package org.vaadin.viritin.grid;

import static org.vaadin.viritin.LazyList.DEFAULT_PAGE_SIZE;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.grid.utils.GridUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.event.SortEvent;
import com.vaadin.event.SortEvent.SortListener;
import com.vaadin.server.Extension;
import com.vaadin.ui.Grid;

/**
 *
 * @param <T> the entity type listed in the Grid
 */
public class MGrid<T> extends Grid {

    private static final long serialVersionUID = -7821775220281254054L;

    private Class<T> typeOfRows;

    public MGrid() {
    }

    /**
     * Creates a new instance of MGrid that contains certain types of rows.
     *
     * @param typeOfRows the type of objects that are listed in the grid
     */
    public MGrid(Class<T> typeOfRows) {
        setRowType(typeOfRows);
    }

    /**
     * Sets the type of the objects used as rows and resets the listing.
     *
     * @param typeOfRows1 the type of objects used as rows
     * @return this
     */
    public MGrid<T> setRowType(Class<T> typeOfRows1) {
        setContainerDataSource(new ListContainer<T>(typeOfRows1));
        this.typeOfRows = typeOfRows1;
        return this;
    }

    /**
     * Creates a new instance of MGrid with given list of rows. Note that if
     * your list might be empty, it is better to use the constructor with the
     * type parameter and then initialize the content with setRows method.
     *
     * @param listOfEntities the (non-empty) list of entities to be displayed in
     * the grid
     */
    public MGrid(List<T> listOfEntities) {
        setRows(listOfEntities);
    }

    /**
     * A shorthand to create MGrid using LazyList. By default page size of
     * LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MGrid(LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        this(new LazyList<T>(pageProvider, countProvider, DEFAULT_PAGE_SIZE));
    }

    /**
     * A shorthand to create MGrid using a LazyList.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     */
    public MGrid(LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(new LazyList<T>(pageProvider, countProvider, pageSize));
    }

    /**
     * A shorthand to create an MGrid using SortableLazyList. By default page
     * size of LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MGrid(SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        this(pageProvider, countProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * A shorthand to create an MGrid using SortableLazyList. By default page
     * size of LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MGrid(SortableLazyList.MultiSortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        this(pageProvider, countProvider, DEFAULT_PAGE_SIZE);
    }

    /**
     * A shorthand to create MTable using SortableLazyList.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     */
    public MGrid(SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(new SortableLazyList<T>(pageProvider, countProvider, pageSize));
        ensureSortListener();
    }

        /**
     * A shorthand to create MTable using SortableLazyList.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @param pageSize the page size (aka maxResults) that is used in paging.
     */
    public MGrid(SortableLazyList.MultiSortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(new SortableLazyList(pageProvider, countProvider, pageSize));
        ensureSortListener();
    }

    private SortListener sortListener;

    private void ensureSortListener() {
        if (sortListener == null) {
            sortListener = new SortEvent.SortListener() {
                private static final long serialVersionUID = -8850456663417023533L;
                
                @Override
                public void sort(SortEvent event) {
                    refreshVisibleRows();
                }
            };
            addSortListener(sortListener);
        }
    }

    /**
     * Enables saving/loading grid settings (visible columns, sort order, etc)
     * to cookies.
     *
     * @param settingsName cookie name where settings are saved should be
     * unique.
     */
    public void attachSaveSettings(String settingsName) {
        GridUtils.attachToGrid(this, settingsName);
    }

    public MGrid<T> setRows(List<T> rows) {
        if (getContainerDataSource() instanceof ListContainer) {
            
            Collection<?> itemIds = getListContainer().getItemIds();
            if (itemIds instanceof SortableLazyList) {
                SortableLazyList<T> old = (SortableLazyList<T>) itemIds;
                if(old.getSortProperty() != null && rows instanceof SortableLazyList ) {
                    SortableLazyList<T> newList = (SortableLazyList<T>) rows;
                    newList.setSortProperty(old.getSortProperty());
                    newList.setSortAscending(old.getSortAscending());
                }
            }
            
            getListContainer().setCollection(rows);
        } else {
            setContainerDataSource(new ListContainer(rows));
        }
        return this;
    }

    public List<T> getRows() {
        return (List<T>) getListContainer().getItemIds();
    }

    protected ListContainer<T> getListContainer() {
        ListContainer<T> listContainer = (ListContainer<T>) getContainerDataSource();
        return listContainer;
    }

    public MGrid<T> setRows(T... rows) {
        setRows(Arrays.asList(rows));
        return this;
    }

    public <P> MGrid<T> withGeneratedColumn(String columnId,
            Class<P> presentationType,
            TypedPropertyValueGenerator.ValueGenerator<T, P> generator) {
        TypedPropertyValueGenerator<T, P> lambdaPropertyValueGenerator
                = new TypedPropertyValueGenerator<>(typeOfRows, presentationType,
                        generator);
        addGeneratedColumn(columnId, lambdaPropertyValueGenerator);
        return this;
    }

    public MGrid<T> withGeneratedColumn(String columnId,
            StringPropertyValueGenerator.ValueGenerator<T> generator) {
        StringPropertyValueGenerator<T> lambdaPropertyValueGenerator
                = new StringPropertyValueGenerator<>(typeOfRows, generator);
        addGeneratedColumn(columnId, lambdaPropertyValueGenerator);
        return this;
    }

    public MGrid<T> withGeneratedColumn(String columnId,
            final PropertyValueGenerator<?> columnGenerator) {
        addGeneratedColumn(columnId, columnGenerator);
        return this;
    }

    private void addGeneratedColumn(String columnId,
            final PropertyValueGenerator<?> columnGenerator) {
        Container.Indexed container = getContainerDataSource();
        GeneratedPropertyListContainer gplc;
        if (container instanceof GeneratedPropertyListContainer) {
            gplc = (GeneratedPropertyListContainer) container;
        } else {
            gplc = new GeneratedPropertyListContainer(typeOfRows);
            try {
                gplc.setCollection(getListContainer().getItemIds());
            } catch (Exception e) {// NOP, not yet set
            }
            setContainerDataSource(gplc);
        }
        gplc.addGeneratedProperty(columnId, columnGenerator);
        addColumn(columnId);
    }

    public MGrid<T> withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

    @Override
    public T getSelectedRow() throws IllegalStateException {
        return (T) super.getSelectedRow();
    }

    /**
     *
     * @param entity the entity (row) to be selected.
     * @return <code>true</code> if the selection state changed,
     * <code>false</code> if the itemId already was selected
     */
    public boolean selectRow(T entity) {
        return select(entity);
    }

    /**
     *
     * @return true if something :-) See parent doc if you REALLY want to use
     * this.
     * @deprecated use the typed selectRow instead
     */
    @Deprecated
    @Override
    public boolean select(Object itemId) throws IllegalArgumentException, IllegalStateException {
        return super.select(itemId);
    }

    public Collection<T> getSelectedRowsWithType() {
        // Maybe this is more complicated than it should be :-)
        return (Collection<T>) super.getSelectedRows();
    }

    public MGrid<T> withProperties(String... propertyIds) {
        Container.Indexed containerDataSource = getContainerDataSource();
        if (containerDataSource instanceof ListContainer) {
            ListContainer<T> lc = (ListContainer<T>) containerDataSource;
            lc.setContainerPropertyIds(propertyIds);
        }
        setColumns((Object[]) propertyIds);
        return this;
    }

    public MGrid<T> withId(String id) {
        setId(id);
        return this;
    }

    private FieldGroup.CommitHandler reloadDataEfficientlyAfterEditor;

    @Override
    public void setEditorEnabled(boolean isEnabled) throws IllegalStateException {
        super.setEditorEnabled(isEnabled);
        ensureRowRefreshListener(isEnabled);

    }

    protected void ensureRowRefreshListener(boolean isEnabled) {
        if (isEnabled && reloadDataEfficientlyAfterEditor == null) {
            reloadDataEfficientlyAfterEditor = new FieldGroup.CommitHandler() {
                private static final long serialVersionUID = -9107206992771475209L;
                
                @Override
                public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                }

                @Override
                public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                    Item itemDataSource = commitEvent.getFieldBinder().
                            getItemDataSource();
                    if (itemDataSource instanceof ListContainer.DynaBeanItem) {
                        ListContainer<T>.DynaBeanItem<T> dynaBeanItem = (ListContainer<T>.DynaBeanItem<T>) itemDataSource;
                        T bean = dynaBeanItem.getBean();
                        refreshRow(bean);
                    }
                }

            };
            getEditorFieldGroup().addCommitHandler(
                    reloadDataEfficientlyAfterEditor);
        }
    }

    /**
     * Manually forces refresh of the row that represents given entity.
     * ListContainer backing MGrid/MTable don't support property change
     * listeners (to save memory and CPU cycles). In some case with Grid, if you
     * know only certain row(s) are changed, you can make a smaller client side
     * change by refreshing rows with this method, instead of refreshing the
     * whole Grid (e.g. by re-assigning the bean list).
     * <p>
     * This method is automatically called if you use "editor row".
     *
     * @param bean the bean whose row should be refreshed.
     */
    public void refreshRow(T bean) {
        Collection<Extension> extensions = getExtensions();
        for (Extension extension : extensions) {
            // Calling with reflection for 7.6-7.5 compatibility
            if (extension.getClass().getName().contains(
                    "RpcDataProviderExtension")) {
                try {
                    Method method = extension.getClass().getMethod(
                            "updateRowData", Object.class);
                    method.invoke(extension, bean);
                    break;
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(MGrid.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Manually forces refresh of the whole data. ListContainer backing
     * MGrid/MTable don't support property change listeners (to save memory and
     * CPU cycles). This method explicitly forces Grid's row cache invalidation.
     */
    public void refreshRows() {
        if (getContainerDataSource() instanceof ListContainer) {
            ListContainer<T> listContainer = getListContainer();
            if (listContainer.getItemIds() instanceof LazyList) {
                ((LazyList) listContainer.getItemIds()).reset();
            }
            listContainer.fireItemSetChange();
        }
        refreshVisibleRows();
    }

    /**
     * Manually forces refresh of all visible rows. ListContainer backing
     * MGrid/MTable don't support property change listeners (to save memory and
     * CPU cycles). This method explicitly forces Grid's row cache invalidation.
     */
    public void refreshVisibleRows() {
        Collection<Extension> extensions = getExtensions();
        for (Extension extension : extensions) {
            // Calling with reflection for 7.6-7.5 compatibility
            if (extension.getClass().getName().contains(
                    "RpcDataProviderExtension")) {
                try {
                    Method method = extension.getClass().getMethod(
                            "refreshCache");
                    method.invoke(extension);
                    break;
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(MGrid.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @return this MTable object
     */
    public MGrid<T> lazyLoadFrom(LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        setRows(new LazyList<T>(pageProvider, countProvider, DEFAULT_PAGE_SIZE));
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
    public MGrid<T> lazyLoadFrom(LazyList.PagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        setRows(new LazyList<T>(pageProvider, countProvider, pageSize));
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @return this MTable object
     */
    public MGrid<T> lazyLoadFrom(
            SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        setRows(new SortableLazyList<T>(pageProvider, countProvider,
                DEFAULT_PAGE_SIZE));
        ensureSortListener();
        return this;
    }

    /**
     * Makes the table lazy load its content with given strategy.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     * @return this MTable object
     */
    public MGrid<T> lazyLoadFrom(
            SortableLazyList.MultiSortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider) {
        setRows(new SortableLazyList(pageProvider, countProvider,
                DEFAULT_PAGE_SIZE));
        ensureSortListener();
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
    public MGrid<T> lazyLoadFrom(
            SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        setRows(new SortableLazyList<T>(pageProvider, countProvider, pageSize));
        ensureSortListener();
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
    public MGrid<T> lazyLoadFrom(
            SortableLazyList.MultiSortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        setRows(new SortableLazyList(pageProvider, countProvider, pageSize));
        ensureSortListener();
        return this;
    }
    
    public MGrid<T> withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MGrid<T> withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MGrid<T> withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MGrid<T> withFullHeight() {
        return withHeight("100%");
    }

    public MGrid<T> withSize(MSize mSize) {
        setWidth(mSize.getWidth(), mSize.getWidthUnit());
        setHeight(mSize.getHeight(), mSize.getHeightUnit());
        return this;
    }

    public MGrid<T> withColumnHeaders(String... header) {
        if (header.length != getColumns().size()) {
            throw new IllegalArgumentException("The length of the headers array must match the number of columns");
        }
        for (int i = 0; i < getColumns().size(); i++) {
            Object propertyId = getColumns().get(i).getPropertyId();
            if (header[i] != null) {
                getColumn(propertyId).setHeaderCaption(header[i]);
            }
        }
        return this;
    }
}
