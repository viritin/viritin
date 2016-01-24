package org.vaadin.viritin.fields;

import java.io.Serializable;

public interface CaptionGenerator<T> extends Serializable {
	
	public String getCaption(T option);
	
}