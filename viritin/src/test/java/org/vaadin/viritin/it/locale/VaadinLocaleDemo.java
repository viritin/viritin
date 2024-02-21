package org.vaadin.viritin.it.locale;

import java.util.*;

import org.vaadin.viritin.components.LocaleSelect;
import org.vaadin.viritin.it.AbstractTest;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.util.VaadinLocale;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import java.time.LocalDate;

/**
 * Demo for the {@link VaadinLocale} class.
 *
 * @author Daniel Nordhoff-Vergien
 *
 */
@SuppressWarnings("serial")
public class VaadinLocaleDemo extends AbstractTest {

    private final VaadinLocale vaadinLocale = new VaadinLocale(Locale.ENGLISH,
            Locale.GERMAN, new Locale("de", "DE"), new Locale("da"), new Locale("fi"));
    private final LocaleSelect localeSelect = (LocaleSelect) new LocaleSelect();
    private final DateField dateField = new DateField();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);
        vaadinLocale.setVaadinRequest(vaadinRequest);
        localeSelect.setItems(vaadinLocale.getSupportedLocales());
        localeSelect.setValue(getLocale());
    }

    @Override
    public Component getTestComponent() {
        dateField.setValue(LocalDate.now());
        localeSelect.setId("language-selection");
        localeSelect.addValueChangeListener(e
                -> vaadinLocale.setLocale(e.getValue())
        );
        Button addNewComponent = new Button("Create new component");

        final MVerticalLayout layout = new MVerticalLayout(localeSelect,
                dateField, new VaadinLocaleDemoComponent(), addNewComponent);

        addNewComponent.addClickListener(
                new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event
            ) {
                layout.add(new VaadinLocaleDemoComponent());
            }
        }
        );
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
