package org.vaadin.viritin.testdomain;

import java.util.Locale;

import org.vaadin.viritin.testdomain.aspect.LocaleAware;
import org.vaadin.viritin.testdomain.aspect.Salutation;

/** 
 * An example session user bean based on a person entity
 * @author Klaus Sausen
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
