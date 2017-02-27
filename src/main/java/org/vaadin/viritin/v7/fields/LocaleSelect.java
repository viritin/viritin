package org.vaadin.viritin.v7.fields;

import org.vaadin.viritin.components.*;
import org.vaadin.viritin.v7.fields.CaptionGenerator;
import org.vaadin.viritin.v7.fields.TypedSelect;

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
public class LocaleSelect extends TypedSelect<Locale> {
    public LocaleSelect() {
        setCaptionGenerator(new CaptionGenerator<Locale>() {
            @Override
            public String getCaption(Locale option) {
                return option.getDisplayName(option);
            }
        });
    }
}