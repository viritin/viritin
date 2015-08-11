package org.vaadin.viritin.grid;

import org.vaadin.viritin.grid.utils.GridUtils;

import com.vaadin.ui.Grid;

public class MGrid extends Grid {

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
    
}
