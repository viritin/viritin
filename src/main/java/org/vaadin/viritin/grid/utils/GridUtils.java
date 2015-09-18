package org.vaadin.viritin.grid.utils;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.viritin.util.BrowserCookie;
import org.vaadin.viritin.util.BrowserCookie.Callback;

import com.vaadin.data.sort.SortOrder;
import com.vaadin.event.SortEvent;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.ColumnReorderEvent;
import org.apache.commons.lang3.StringUtils;

/**
 * Use this class to save grid hidden columns to cookies. Use {@link #attachToGrid(Grid, String)
 * }
 *
 */
public class GridUtils {

    private static final String COLOMN_DELIMITER = ":";
    private static final String SEMI_COLOMN_DELIMITER = ";";
    private final String HIDDEN_SETTINGS_NAME;
    private final String SORT_ORDER_SETTINGS_NAME;
    private final String COLUMNS_ORDER_SETTINGS_NAME;
    private final List<String> columnsOrder = new ArrayList<String>();
    private final Grid grid;

    /**
     * Set specified grid to save hidden columns in cookies.
     *
     * @param grid - grid which columns would be used
     * @param cookieName - name of the cookie. Should be unique for every grid.
     */
    static public void attachToGrid(Grid grid, String cookieName) {
        GridUtils utils = new GridUtils(grid, cookieName);
    }

    private GridUtils(Grid grid, String cookieName) {
        super();
        this.grid = grid;
        HIDDEN_SETTINGS_NAME = cookieName + "hiddenCols";
        SORT_ORDER_SETTINGS_NAME = cookieName + "sortOrder";
        COLUMNS_ORDER_SETTINGS_NAME = cookieName + "columnOrder";
        loadSettings();
        
        grid.addColumnVisibilityChangeListener(
                new Grid.ColumnVisibilityChangeListener() {

            @Override
            public void columnVisibilityChanged(
                    Grid.ColumnVisibilityChangeEvent event) {
                saveHidden();
            }
        });
        
        grid.addSortListener(new SortEvent.SortListener() {

            @Override
            public void sort(SortEvent event) {
                saveSortOrder();
            }
        });
        grid.addColumnReorderListener(new Grid.ColumnReorderListener() {

            @Override
            public void columnReorder(ColumnReorderEvent event) {
                saveColumnOrder(event);
            }
        });
    }

    private void saveColumnOrder(ColumnReorderEvent e) {
        //Number of columns not more than 1000, hopefully :)
        //This operation don't need to be fast, that's why were recreate the cookie value
        //every time.

        StringBuilder sb = new StringBuilder();
        for (Column c : grid.getColumns()) {
            sb.append(c.getPropertyId().toString());
            sb.append(COLOMN_DELIMITER);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        BrowserCookie.setCookie(COLUMNS_ORDER_SETTINGS_NAME, sb.toString());
    }

    private void loadSettings() {
        loadHidden();
        loadSortOrder();
        loadColumnOrder();
    }

    private void loadColumnOrder() {
        Callback saveFunc = new Callback() {
            @Override
            public void onValueDetected(String value) {
                if (value != null) {
                    String[] columnsOrder = value.split(COLOMN_DELIMITER);
                    grid.setColumnOrder(columnsOrder);
                }
            }
        };
        BrowserCookie.detectCookieValue(COLUMNS_ORDER_SETTINGS_NAME, saveFunc);

    }

    private void loadSortOrder() {
        Callback saveFunc = new Callback() {
            @Override
            public void onValueDetected(String value) {
                List<SortOrder> sortOrderList = new ArrayList<SortOrder>();
                if (!StringUtils.isEmpty(value)) {
                    String[] sortOrder = value.split(SEMI_COLOMN_DELIMITER);
                    for (String so : sortOrder) {
                        String[] colIdWithSort = so.split(COLOMN_DELIMITER);
                        Object propertyId = colIdWithSort[0];
                        SortDirection direction = SortDirection.valueOf(
                                colIdWithSort[1]);
                        SortOrder soCreated = new SortOrder(propertyId,
                                direction);
                        sortOrderList.add(soCreated);
                    };
                }
                grid.setSortOrder(sortOrderList);
            }
        };
        BrowserCookie.detectCookieValue(SORT_ORDER_SETTINGS_NAME, saveFunc);
    }

    private void loadHidden() {
        Callback saveFunc = new Callback() {
            @Override
            public void onValueDetected(String value) {
                if (value != null) {
                    for (String col : value.split(COLOMN_DELIMITER)) {
                        Column column = grid.getColumn(col);
                        if (column != null) {
                            column.setHidden(true);
                        }
                    }
                }
            }
        };
        BrowserCookie.detectCookieValue(HIDDEN_SETTINGS_NAME, saveFunc);
    }

    private void saveSortOrder() {
        final List<SortOrder> sortOrder = grid.getSortOrder();
        StringBuilder sb = new StringBuilder();
        for (SortOrder o : sortOrder) {
            sb.append(o.getPropertyId());
            sb.append(COLOMN_DELIMITER);
            sb.append(o.getDirection());
            sb.append(SEMI_COLOMN_DELIMITER);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        BrowserCookie.setCookie(SORT_ORDER_SETTINGS_NAME, sb.toString());
    }

    private void saveHidden() {
        StringBuilder sb = new StringBuilder();
        for (Column c : grid.getColumns()) {
            if (c.isHidden()) {
                sb.append(c.getPropertyId());
                sb.append(COLOMN_DELIMITER);
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        BrowserCookie.setCookie(HIDDEN_SETTINGS_NAME, sb.toString());
    }

}
