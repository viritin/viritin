package org.vaadin.viritin.fields;

import com.vaadin.server.Resource;
import java.io.Serializable;

/**
 * A strategy to provide icon for options in selects.
 * 
 * @param <T> the type of bean for which the icon is to be generated
 */
public interface IconGenerator<T> extends Serializable {
	
	public Resource getIcon(T option);
	
}