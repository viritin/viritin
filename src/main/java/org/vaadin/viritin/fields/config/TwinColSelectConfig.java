package org.vaadin.viritin.fields.config;

import com.vaadin.ui.TwinColSelect;

/**
 * Configures fluently the TwinColSelect within TypedSelect
 */
public class TwinColSelectConfig {

    private Integer rows;
    private String leftColumnCaption;
    private String rightColumnCaption;

    public static final TwinColSelectConfig build() {
        return new TwinColSelectConfig();
    }

    public TwinColSelectConfig withRows(int rows) {
        this.rows = rows;
        return this;
    }

    public TwinColSelectConfig withLeftColumnCaption(String leftColumnCaption) {
        this.leftColumnCaption = leftColumnCaption;
        return this;
    }

    public TwinColSelectConfig withRightColumnCaption(String rightColumnCaption) {
        this.rightColumnCaption = rightColumnCaption;
        return this;
    }

    public void configurateTwinColSelect(TwinColSelect twinColSelect) {
        if (rows != null) {
            twinColSelect.setRows(rows);
        }
        if (leftColumnCaption != null) {
            twinColSelect.setLeftColumnCaption(leftColumnCaption);
        }
        if (rightColumnCaption != null) {
            twinColSelect.setRightColumnCaption(rightColumnCaption);
        }
    }
}