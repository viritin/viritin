package org.vaadin.viritin.v7.fields.config;

import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.ComboBox;

/**
 * Configures fluently the ComboBox within TypedSelect
 */
public class ComboBoxConfig {
    FilteringMode filteringMode;
    ComboBox.ItemStyleGenerator itemStyleGenerator;
    Integer pageLength;
    Boolean textInputAllowed;
    Boolean scrollToSelectedItem;

    public static final ComboBoxConfig build() {
        return new ComboBoxConfig();
    }

    public ComboBoxConfig withFilteringMode(FilteringMode filteringMode) {
        this.filteringMode = filteringMode;
        return this;
    }

    public ComboBoxConfig withItemStyleGenerator(ComboBox.ItemStyleGenerator itemStyleGenerator) {
        this.itemStyleGenerator = itemStyleGenerator;
        return this;
    }

    public ComboBoxConfig withPageLength(int pageLength) {
        this.pageLength = pageLength;
        return this;
    }

    public ComboBoxConfig withTextInputAllowed(boolean textInputAllowed) {
        this.textInputAllowed = textInputAllowed;
        return this;
    }

    public ComboBoxConfig withScrollToSelectedItem(boolean scrollToSelectedItem) {
        this.scrollToSelectedItem = scrollToSelectedItem;
        return this;
    }

    public void configurateComboBox(ComboBox comboBox) {
        if (filteringMode != null) {
            comboBox.setFilteringMode(filteringMode);
        }
        if (itemStyleGenerator != null) {
            comboBox.setItemStyleGenerator(itemStyleGenerator);
        }
        if (pageLength != null) {
            comboBox.setPageLength(pageLength);
        }
        if (textInputAllowed != null) {
            comboBox.setTextInputAllowed(textInputAllowed);
        }
        if (scrollToSelectedItem != null) {
            comboBox.setScrollToSelectedItem(scrollToSelectedItem);
        }
    }
}