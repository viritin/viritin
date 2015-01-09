package org.vaadin.viritin.fields;

public interface CaptionGenerator<T> {
	
	public String getCaption(T option);
	
}