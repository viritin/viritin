package org.vaadin.viritin.util;

import java.util.*;
import java.util.Locale.LanguageRange;

import com.vaadin.server.*;
import com.vaadin.ui.*;

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
    private static final String LOCALE_SESSION_ATTRIBUTE = "org.vaadin.viritin.selectedLocale";
    private final List<Locale> supportedLocales = new ArrayList<Locale>();
    private Locale bestLocaleByAcceptHeader;

    public VaadinLocale(Locale... supportedLocales) {
        if (supportedLocales == null || supportedLocales.length == 0) {
            throw new IllegalArgumentException(
                    "At least one locale must be supported");
        }

        for (Locale locale : supportedLocales) {
            this.supportedLocales.add(locale);
        }
    }

    /**
     * Instantiates a new VaadinLocale object
     * 
     * @param vaadinRequest
     * @param supportedLocales
     *            At least one Locale which the application supports. The first
     *            locale is the default locale, if negotiation fails.
     * 
     * 
     * @throws IllegalArgumentException
     *             if there is no locale.
     */
    public VaadinLocale(VaadinRequest vaadinRequest, Locale... supportedLocales) {
        this(supportedLocales);
        setVaadinRequest(vaadinRequest);
        updateVaadinLocale();
    }

    public void setVaadinRequest(VaadinRequest vaadinRequest) {
        if (vaadinRequest == null) {
            throw new IllegalArgumentException("VaadinRequest is needed!");
        }

        String languages = vaadinRequest.getHeader("Accept-Language");
        List<LanguageRange> priorityList = Locale.LanguageRange
                .parse(languages);
        bestLocaleByAcceptHeader = Locale.lookup(priorityList,
                this.supportedLocales);
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
        Stack<Component> stack = new Stack<Component>();
        stack.push(UI.getCurrent());
        while (!stack.isEmpty()) {
            Component component = stack.pop();
            if (component instanceof HasComponents) {
                for (Iterator<Component> i = ((HasComponents) component)
                        .iterator(); i.hasNext();)
                    stack.add(i.next());
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
        return new ArrayList<Locale>(supportedLocales);
    }
}
