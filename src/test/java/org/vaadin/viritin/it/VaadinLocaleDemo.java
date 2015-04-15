package org.vaadin.viritin.it;

import java.util.*;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.components.LocaleSelect;
import org.vaadin.viritin.fields.*;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.VaadinLocale;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

/**
 * 
 * @author Daniel Nordhoff-Vergien
 *
 */
@Theme("valo")
public class VaadinLocaleDemo extends AbstractTest {
    private VaadinLocale vaadinLocale = new VaadinLocale(Locale.ENGLISH,
            Locale.GERMAN, new Locale("de", "DE"), new Locale("da"));
    private LocaleSelect localeSelect = (LocaleSelect) new LocaleSelect()
            .withSelectType(ComboBox.class);;
    private DateField dateField = new DateField();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);
        vaadinLocale.setVaadinRequest(vaadinRequest);
        localeSelect.setOptions(vaadinLocale.getSupportedLocales());
        localeSelect.setValue(vaadinLocale.getLocale());
    }

    @Override
    public Component getTestComponent() {
        dateField.setValue(new Date());
        localeSelect.setValue(getLocale());
        localeSelect.setId("language-selection");
        localeSelect
                .addMValueChangeListener(new MValueChangeListener<Locale>() {
                    @Override
                    public void valueChange(MValueChangeEvent<Locale> event) {
                        vaadinLocale.setLocale(event.getValue());
                    }
                });
        return new MVerticalLayout(localeSelect, dateField);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "VaadinLocaleDemo", locale);
        this.localeSelect.setCaption(resourceBundle.getString("language"));
        dateField.setCaption(resourceBundle.getString("date"));
    }

    @Override
    public String getDescription() {
        return "Demo for locale handling.";
    }
}
