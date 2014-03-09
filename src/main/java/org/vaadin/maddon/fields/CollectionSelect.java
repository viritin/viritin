package org.vaadin.maddon.fields;

import java.util.Collection;
import java.util.HashSet;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.ListSelect;

/**
 * TODO improve this, just copy pasted from archived SmartFields addon.
 * 
 * @author mstahv
 * @param <T> 
 */
public class CollectionSelect<T> extends CustomField<Collection<T>> {

	private ListSelect select = new ListSelect() {

		@SuppressWarnings("unchecked")
		public String getItemCaption(Object option) {
			if (captionGenerator != null) {
				return captionGenerator.getCaption((T) option);
			}
			return super.getItemCaption(option);
		};
	};
	private CaptionGenerator<T> captionGenerator;

	@Override
	protected Component initContent() {
		return select;
	}

	public CollectionSelect() {
		select.setMultiSelect(true);
		select.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				/*
				 * Modify the original collection to make it possible for e.g.
				 * ORM tools to optimize queries
				 */

				Collection<T> collection = getInternalValue();
				HashSet<T> orphaned = new HashSet<T>(collection);

				@SuppressWarnings("unchecked")
				Collection<T> newValueSet = (Collection<T>) select.getValue();
				for (T t : newValueSet) {
					orphaned.remove(t);
					if (!collection.contains(t)) {
						collection.add(t);
					}
				}
				collection.removeAll(orphaned);
				CollectionSelect.super.setInternalValue(collection);
				fireValueChange(true);
			}
		});
	}

	public CollectionSelect(Collection<T> options) {
		this();
		setOptions(options);
	}
	public CollectionSelect(String caption, Collection<T> options) {
		this(caption);
		setOptions(options);
	}

	public CollectionSelect(String caption) {
		this();
		setCaption(caption);
	}

	@SuppressWarnings("deprecation")
	public void setOptions(Collection<T> options) {
		select.setContainerDataSource(new BeanItemContainer<T>(options));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Collection<T>> getType() {
		try {
			return getPropertyDataSource().getType();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void setInternalValue(Collection<T> newValue) {
		super.setInternalValue(newValue);
		select.setValue(newValue);
	}

	public CaptionGenerator<T> getCaptionGenerator() {
		return captionGenerator;
	}

	public void setCaptionGenerator(CaptionGenerator<T> captionGenerator) {
		this.captionGenerator = captionGenerator;
	}

}