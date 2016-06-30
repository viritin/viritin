package org.vaadin.viritin.fields.config;

import com.vaadin.ui.ListSelect;

/**
 * Configures fluently the ListSelect within TypedSelect
 */
public class ListSelectConfig {

    Integer rows;

    public static final ListSelectConfig build() {
        return new ListSelectConfig();
    }

    public ListSelectConfig withRows(int rows) {
        this.rows = rows;
        return this;
    }

    public void configurateListSelect(ListSelect listSelect) {
        if (rows != null) {
            listSelect.setRows(rows);
        }
    }
}
