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
package org.vaadin.viritin.testdomain;

import java.util.Locale;

import org.vaadin.viritin.testdomain.aspect.LocaleAware;
import org.vaadin.viritin.testdomain.aspect.Salutation;

/** 
 * An example session user bean based on a person entity
 */
public class User 
	implements LocaleAware, Salutation 
{
	private int personId;
	private transient Person person;
	
	private Locale locale;
	
	public User() {
		super();
		locale = Locale.US;
	}
	
	public User(Person personParameter) {
		this();
		person = personParameter;
	}

	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale localeParam) {
		locale = localeParam;
	}
	
	public Person getPerson() {
		if (person == null) {
			person = new Person(); 
			//e.g. if the session was serialized, reload the person by id 
			person.setId(personId);
		}
		return person;
	}
	
}
