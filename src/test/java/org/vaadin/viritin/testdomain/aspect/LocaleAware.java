package org.vaadin.viritin.testdomain.aspect;

import java.util.Locale;

/**
 * Locale aware objects, such as the logged-in user, etc.
 * 
 */
public interface LocaleAware {

	Locale getLocale();
}
