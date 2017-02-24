package org.vaadin.viritin.it.locale;

import com.vaadin.ui.CustomComponent;
import java.util.*;

import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.v7.ui.*;
/**
 * This is an example how components can change their locale
 * @author Daniel Nordhoff-Vergien
 *
 */
public class VaadinLocaleDemoComponent extends CustomComponent {
    private final DateField dateField = new DateField();

    public VaadinLocaleDemoComponent() {
        dateField.setValue(new Date());
        setCompositionRoot(new MVerticalLayout(new Label(
                VaadinLocaleDemoComponent.class.getSimpleName()), dateField));
        // don't call getLocale() here, because it will return null until the
        // component is attached to a parent
    }

    // Called when the component is attached to a parent. Before this
    // getLocale() will return null.
    @Override
    public void attach() {
        super.attach();
        updateStrings();
    }

    // Called when the locale is changed
    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        updateStrings();
    }

    /**
     * Method in which strings are updated depending on the current locale
     */
    private void updateStrings() {
        // lang part only
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "VaadinLocaleDemo", getLocale(), VaadinLocaleDemoComponent.class.getClassLoader());
        dateField.setCaption(resourceBundle.getString("date"));
    }
}
