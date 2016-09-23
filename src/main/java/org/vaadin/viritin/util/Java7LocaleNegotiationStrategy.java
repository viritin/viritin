package org.vaadin.viritin.util;

import com.vaadin.server.VaadinRequest;
import org.vaadin.viritin.util.VaadinLocale.LocaleNegotiationStrategey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Java7LocaleNegotiationStrategy implements
        LocaleNegotiationStrategey {

    @Override
    public Locale negotiate(final List<Locale> supportedLocales,
            VaadinRequest vaadinRequest) {
        String languages = vaadinRequest.getHeader("Accept-Language");
        ArrayList<Locale> preferredArray = new ArrayList<>(
                supportedLocales);
        if (languages != null) {
            final String[] priorityList = languages.split(",");

            Collections.sort(preferredArray, new Comparator<Locale>() {

                @Override
                public int compare(Locale o1, Locale o2) {
                    int pos1 = supportedLocales.size(), pos2 = supportedLocales
                            .size();
                    for (int i = 0; i < priorityList.length; i++) {
                        String lang = priorityList[i].split("[_;-]")[0].trim();
                        if (lang.equals(o1.getLanguage())) {
                            pos1 = i;
                        }
                        if (lang.equals(o2.getLanguage())) {
                            pos2 = i;
                        }
                    }
                    return pos1 - pos2;
                }
            });
        }
        return preferredArray.get(0);
    }
}
