/*
 * Copyright 2016 Klaus Sausen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
