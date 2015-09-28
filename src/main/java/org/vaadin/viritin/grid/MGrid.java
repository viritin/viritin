package org.vaadin.viritin.grid;

import org.vaadin.viritin.grid.utils.GridUtils;

import com.vaadin.ui.Grid;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.vaadin.viritin.ListContainer;

/**
 *
 * @param <T> the entity type listed in the Grid
 */
public class MGrid<T> extends Grid {

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
     *         <code>false</code> if the itemId already was selected
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
