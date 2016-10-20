package org.vaadin.viritin.util;

import com.vaadin.server.VaadinRequest;
import org.vaadin.viritin.util.VaadinLocale.LocaleNegotiationStrategey;

import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * Simple implementation of {@link LocaleNegotiationStrategey} which works with
 * java versions before 1.8.
 * <p>
 * It uses the {@link Locale} objects from the {@link VaadinRequest} and returns
 * the first one wihch is equal to one in the supportedLocales. If there is no
 * match is ueses the first one with has the same language as one of the
 * supported ones.
 * 
 * @author Daniel Nordhoff-Vergien
 *
 */
public class SimpleLocaleNegotiationStrategy implements
        LocaleNegotiationStrategey {
    @Override
    public Locale negotiate(List<Locale> supportedLocales,
            VaadinRequest vaadinRequest) {
        Enumeration<Locale> acceptedLocales = vaadinRequest.getLocales();
        while (acceptedLocales.hasMoreElements()) {
            Locale locale = acceptedLocales.nextElement();
            if (supportedLocales.contains(locale)) {
                return locale;
            }
        }
        acceptedLocales = vaadinRequest.getLocales();
        while (acceptedLocales.hasMoreElements()) {
            Locale locale = acceptedLocales.nextElement();
            for (Locale supportedLocale : supportedLocales) {
                if (locale.getLanguage().equals(supportedLocale.getLanguage())) {
                    return locale;
                }
            }
        }
        return null;
    }
}
