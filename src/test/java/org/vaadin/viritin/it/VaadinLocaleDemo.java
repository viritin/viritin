package org.vaadin.viritin.it;

import java.util.*;

import org.vaadin.addonhelpers.AbstractTest;
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
    private VaadinLocale vaadinLocale;
    private ComboBox languageComboBox = new ComboBox();
    private DateField dateField = new DateField();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        vaadinLocale = new VaadinLocale(vaadinRequest, Locale.ENGLISH,
                Locale.GERMAN);
        super.init(vaadinRequest);
    }

    @Override
    public Component getTestComponent() {
        dateField.setValue(new Date());
        languageComboBox.setImmediate(true);
        languageComboBox.addItems(vaadinLocale.getSupportedLocales().toArray());
        languageComboBox.setValue(vaadinLocale.getLocale());
        languageComboBox.setId("language-selection");
        languageComboBox.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                vaadinLocale.setLocale((Locale) languageComboBox.getValue());
            }
        });
        return new MVerticalLayout(languageComboBox, dateField);
    }

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "VaadinLocaleDemo", locale);
        this.languageComboBox.setCaption(resourceBundle.getString("language"));
        dateField.setCaption(resourceBundle.getString("date"));
    }

    @Override
    public String getDescription() {
        return "Demo for locale handling.";
    }
}
