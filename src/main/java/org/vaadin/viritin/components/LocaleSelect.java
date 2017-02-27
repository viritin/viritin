package org.vaadin.viritin.components;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;
import java.util.Locale;

/**
 * A select component for {@link java.util.Locale}.
 * <p>
 * The caption for each locale is the name of the locale in its own language
 * e.g. "Deutsch" for german and "Dansk" for danish.
 * <p>
 * @author Daniel Nordhoff-Vergien
 *
 */
@SuppressWarnings("serial")
public class LocaleSelect extends ComboBox<Locale> {
    public LocaleSelect() {
        setItemCaptionGenerator(new ItemCaptionGenerator<Locale>() {
            @Override
            public String apply(Locale option) {
                return option.getDisplayName(option);
            }
        });
    }
}