package org.vaadin.viritin.util;

import com.vaadin.server.*;
import com.vaadin.ui.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

/**
 * This class handles the locale for a vaadin application. It negotiates the
 * locale based on
 * <ol>
 * <li>a selected locale via {@link #setLocale(Locale)}</li>
 * <li>the best fitting one by the Accept-Lanuage Header</li>
 * <li>the first one from the supported ones</li>
 * </ol>
 * After a locale has been selected a call of
 * {@link com.vaadin.ui.AbstractComponent#setLocale(Locale)} on all components
 * in the current {@link com.vaadin.ui.UI} is triggered. You can update your
 * strings there.
 *
 * @author Daniel Nordhoff-Vergien
 *
 */
public class VaadinLocale {

    public interface LocaleNegotiationStrategey {

        /**
         * Returns the best fitting supported locale depending on the http
         * language accept header.
         *
         * @param supportedLocales the list of supported locales
         * @param vaadinRequest the current VaadinRequest
         * @return the best fitting supported locale
         */
        public Locale negotiate(List<Locale> supportedLocales,
                VaadinRequest vaadinRequest);
    }

    private static final String LOCALE_SESSION_ATTRIBUTE = "org.vaadin.viritin.selectedLocale";
    private final List<Locale> supportedLocales = new ArrayList<>();
    private Locale bestLocaleByAcceptHeader;
    private final LocaleNegotiationStrategey localeNegotiationStrategey;

    public VaadinLocale(LocaleNegotiationStrategey localeNegotiationStrategey,
            Locale... supportedLocales) {
        if (supportedLocales == null || supportedLocales.length == 0) {
            throw new IllegalArgumentException(
                    "At least one locale must be supported");
        }
        if (localeNegotiationStrategey == null) {
            throw new IllegalArgumentException(
                    "localeNegotiatinStrategy may not be null!");
        }
        this.localeNegotiationStrategey = localeNegotiationStrategey;

        this.supportedLocales.addAll(Arrays.asList(supportedLocales));
    }

    /**
     * Instantiates a new VaadinLocale object
     *
     * @param localeNegotiationStrategey the localeNegotiationStrategey the
     * should be used to detect the most suitable locale
     * @param vaadinRequest the current vaadinRequest, for detection 
     * @param supportedLocales At least one Locale which the application
     * supports. The first locale is the default locale, if negotiation fails.
     *
     *
     * @throws IllegalArgumentException if there is no locale.
     */
    public VaadinLocale(LocaleNegotiationStrategey localeNegotiationStrategey,
            VaadinRequest vaadinRequest, Locale... supportedLocales) {
        this(localeNegotiationStrategey, supportedLocales);
        setVaadinRequest(vaadinRequest);
        updateVaadinLocale();
    }

    /**
     * Creates a new instance with the {@link Java7LocaleNegotiationStrategy}.
     *
     * @param supportedLocales the list of supported locales
     */
    public VaadinLocale(Locale... supportedLocales) {
        this(new Java7LocaleNegotiationStrategy(), supportedLocales);
    }

    public void setVaadinRequest(VaadinRequest vaadinRequest) {
        if (vaadinRequest == null) {
            throw new IllegalArgumentException("VaadinRequest is needed!");
        }
        bestLocaleByAcceptHeader = localeNegotiationStrategey.negotiate(
                supportedLocales, vaadinRequest);
        updateVaadinLocale();
    }

    public void setLocale(Locale locale) {
        if (locale != null && !supportedLocales.contains(locale)) {
            throw new IllegalArgumentException("Locale " + locale
                    + " is not supported.");
        }
        if (locale == null) {
            VaadinSession.getCurrent().setAttribute(LOCALE_SESSION_ATTRIBUTE,
                    null);
        } else {
            VaadinSession.getCurrent().setAttribute(LOCALE_SESSION_ATTRIBUTE,
                    locale.toLanguageTag());
        }
        updateVaadinLocale();
    }

    public void unsetLocale() {
        setLocale(null);
    }

    private void updateVaadinLocale() {
        Locale locale = getLocale();
        UI.getCurrent().setLocale(locale);
        VaadinSession.getCurrent().setLocale(locale);
        recursiveSetLocale();
    }

    private void recursiveSetLocale() {
        Stack<Component> stack = new Stack<>();
        stack.addAll(VaadinSession.getCurrent().getUIs());
        while (!stack.isEmpty()) {
            Component component = stack.pop();
            if (component instanceof HasComponents) {
                for (Iterator<Component> i = ((HasComponents) component)
                        .iterator(); i.hasNext();) {
                    stack.add(i.next());
                }
            }
            if (component instanceof AbstractComponent) {
                AbstractComponent abstractComponent = (AbstractComponent) component;
                abstractComponent.setLocale(UI.getCurrent().getLocale());
            }
        }
    }

    public Locale getLocale() {
        String locale = (String) VaadinSession.getCurrent().getAttribute(
                LOCALE_SESSION_ATTRIBUTE);
        if (locale != null) {
            return Locale.forLanguageTag(locale);
        } else if (bestLocaleByAcceptHeader != null) {
            return bestLocaleByAcceptHeader;
        } else {
            return supportedLocales.get(0);
        }
    }

    public Locale getBestLocaleByAcceptHeader() {
        return bestLocaleByAcceptHeader;
    }

    public List<Locale> getSupportedLocales() {
        return new ArrayList<>(supportedLocales);
    }
}
