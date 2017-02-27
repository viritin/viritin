package org.vaadin.viritin.v7.fields;

import java.io.Serializable;

/**
 * A strategy to provide caption for options.
 * 
 * @param <T> the type bean/option for which the caption is to be generated
 */
public interface CaptionGenerator<T> extends Serializable {
	
	public String getCaption(T option);
	
}