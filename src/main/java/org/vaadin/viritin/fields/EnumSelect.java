package org.vaadin.viritin.fields;

import java.util.Arrays;
import java.util.Collection;

import org.vaadin.viritin.fluency.ui.FluentAbstractField;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ItemCaptionGenerator;

public class EnumSelect<T extends Enum<T>> extends ComboBox<T> implements FluentAbstractField<EnumSelect<T>, T> {
	private static final long serialVersionUID = 6512803610855055709L;

	public EnumSelect(Class<T> enumClass) {
		this(null, enumClass);
	}

	public EnumSelect(String caption, Class<T> enumClass) {
		super(caption, getConstantList(enumClass));		
	}
	
	public EnumSelect<T> withItemCaptionGenerator(ItemCaptionGenerator<T> itemCaptionGenerator) {
		setItemCaptionGenerator(itemCaptionGenerator);
		return this;
	}
	
	private static <T extends Enum<T>> Collection<T> getConstantList(Class<T> enumClass) {
		final T[] constants=enumClass.getEnumConstants();
		return Arrays.asList(constants);
	}
}
