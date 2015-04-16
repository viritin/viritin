package org.vaadin.viritin.it.locale;

import java.util.*;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.components.LocaleSelect;
import org.vaadin.viritin.fields.*;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.VaadinLocale;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Demo for the {@link VaadinLocale} class.
 * 
 * @author Daniel Nordhoff-Vergien
 *
 */
@SuppressWarnings("serial")
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
        Button addNewComponent = new Button("Create new component");

        final MVerticalLayout layout = new MVerticalLayout(localeSelect,
                dateField, new VaadinLocaleDemoComponent(), addNewComponent);
        addNewComponent.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                layout.add(new VaadinLocaleDemoComponent());
            }
        });
        return layout;
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        updateStrings();
    }

    private void updateStrings() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "VaadinLocaleDemo", getLocale());
        this.localeSelect.setCaption(resourceBundle.getString("language"));
        dateField.setCaption(resourceBundle.getString("date"));
    }

    @Override
    public String getDescription() {
        return "Demo for locale handling.";
    }
}
