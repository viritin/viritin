package org.vaadin.viritin.fields.config;

import com.vaadin.v7.ui.OptionGroup;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Configures fluently the OptionGroup within TypedSelect
 */
public class OptionGroupConfig {

    Boolean htmlContentAllowed;
    Set<Object> disabledItemIds;

    public static final OptionGroupConfig build() {
        return new OptionGroupConfig();
    }

    public OptionGroupConfig withHtmlContentAllowed(boolean htmlContentAllowed) {
        this.htmlContentAllowed = htmlContentAllowed;
        return this;
    }

    public OptionGroupConfig withDisabledItemIds(List<Object> objectList) {
        if (disabledItemIds == null) {
            disabledItemIds = new HashSet<>();
        }
        disabledItemIds.add(objectList);
        return this;
    }

    public OptionGroupConfig withDisabledItemIds(Object... objects) {
        return withDisabledItemIds(Arrays.asList(objects));
    }

    public void configurateOptionGroup(OptionGroup optionGroup) {
        if (htmlContentAllowed != null) {
            optionGroup.setHtmlContentAllowed(htmlContentAllowed);
        }
        if (disabledItemIds != null) {
            for (Object o : disabledItemIds) {
                optionGroup.setItemEnabled(o, false);
            }
        }
    }
}
