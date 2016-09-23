package org.vaadin.viritin.util;

import java.lang.reflect.Method;
import java.util.*;

import org.vaadin.viritin.util.VaadinLocale.LocaleNegotiationStrategey;

import com.vaadin.server.VaadinRequest;
import java.lang.reflect.InvocationTargetException;

/**
 * Implementation of {@link LocaleNegotiationStrategey} which uses java 1.8
 * {@link Locale#lookup(List, Collection)} to negotiate the best locale.
 * 
 * @author Daniel Nordhoff-Vergien
 *
 */
public class Java8LocaleNegotiationStrategy implements
        LocaleNegotiationStrategey {

    @Override
    public Locale negotiate(List<Locale> supportedLocales,
            VaadinRequest vaadinRequest) {
        String languages = vaadinRequest.getHeader("Accept-Language");
        try {
            // Use reflection here, so the code compiles with jdk 1.7
            Class<?> languageRange = Class
                    .forName("java.util.Locale$LanguageRange");
            Method parse = languageRange.getMethod("parse", String.class);
            Object priorityList = parse.invoke(null, languages);
            Method lookup = Locale.class.getMethod("lookup", List.class,
                    Collection.class);
            return (Locale) lookup.invoke(null, priorityList, supportedLocales);
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException(
                    "Java8LocaleNegotiontionStrategy need java 1.8 or newer.",
                    e);
        }

    }
}
