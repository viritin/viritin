package org.vaadin.viritin.grid;

import org.vaadin.viritin.grid.utils.GridUtils;

import com.vaadin.ui.Grid;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.vaadin.viritin.LazyList;
import static org.vaadin.viritin.LazyList.DEFAULT_PAGE_SIZE;
import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.SortableLazyList;

/**
 *
 * @param <T> the entity type listed in the Grid
 */
public class MGrid<T> extends Grid {

    public MGrid() {
    }

    /**
     * Creates a new instance of MGrid that contains certain types of rows.
     *
     * @param typeOfRows the type of entities that are listed in the grid
     */
    public MGrid(Class<T> typeOfRows) {
        setContainerDataSource(new ListContainer(typeOfRows));
    }
    
    /**
     * Creates a new instance of MGrid with given list of rows.
     *
     * @param listOfEntities the list of entities to be displayed in the grid
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
        this(new LazyList(pageProvider, countProvider, DEFAULT_PAGE_SIZE));
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
        this(new LazyList(pageProvider, countProvider, pageSize));
    }

    /**
     * A shorthand to create an MGrid using SortableLazyList. By default page size
     * of LazyList.DEFAULT_PAGE_SIZE (30) is used.
     *
     * @param pageProvider the interface via entities are fetched
     * @param countProvider the interface via the count of items is detected
     */
    public MGrid(SortableLazyList.SortablePagingProvider<T> pageProvider,
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
    public MGrid(SortableLazyList.SortablePagingProvider<T> pageProvider,
            LazyList.CountProvider countProvider, int pageSize) {
        this(new SortableLazyList(pageProvider, countProvider, pageSize));
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
        setContainerDataSource(new ListContainer(rows));
        return this;
    }

    public MGrid<T> setRows(T... rows) {
        setContainerDataSource(new ListContainer(Arrays.asList(rows)));
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

}
