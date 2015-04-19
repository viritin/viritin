package org.vaadin.viritin.util;

import java.util.*;
import java.util.Locale.LanguageRange;

import org.vaadin.viritin.util.VaadinLocale.LocaleNegotiationStrategey;

import com.vaadin.server.VaadinRequest;

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
        List<LanguageRange> priorityList = Locale.LanguageRange
                .parse(languages);
        return Locale.lookup(priorityList, supportedLocales);
    }
}
