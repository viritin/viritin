package org.vaadin.viritin.testdomain.aspect;

import java.util.Locale;


/**
 * This is a possibility how to add a read-only aspect with 
 * Java 8 default methods to several entities at once 
 * by only implementing the interface.
 * If the test class {@link org.vaadin.viritin.testdomain.Person} 
 * had a gender the salutation could even be done gender specific.
 * 
 * @author Klaus Sausen
 */
public interface Salutation extends LocaleAware {

	/**
	 * (Usually the texts are in external resources)
	 * @return a localized salutation
	 */
	default String getLocalizedSalutation() {
		
		Locale locale = getLocale();
		if (locale == null) {
			return null;
		}
		String language = locale.getLanguage();
		if (language.equals(Locale.ENGLISH.getLanguage())) {
			return "Dear {0},";
		} 
		else if (language.equals(Locale.FRANCE.getLanguage())) {
			return "Cher {0},";
		}
		return "{0},";
	
	}
	
}
