package org.vaadin.viritin.fields;

import org.vaadin.viritin.label.Header;

/**
 * A field which represents the value always in a header, so it is not editable.
 * <p>
 * The use case is a non editable value in a form, e.g. an id.
 * <p>
 * The creation of the header can be controlled via a
 * {@link org.vaadin.viritin.fields.CaptionGenerator}.
 *
 * @author Birke Heeren
 *
 * @param <T> the type of the entity
 */
public class HeaderField<T> extends LabelField<T> {

    private static final long serialVersionUID = -6461526936461789654L;

    public HeaderField(String caption) {
	super(caption);
	super.setLabel(new Header(""));
    }

    public HeaderField() {
	super.setLabel(new Header(""));
    }

    @Override
    public Header getLabel() {
	return (Header) super.getLabel();
    }
    
    public void setLabel(Header label) {
	super.setLabel(label);
    }

    public void setHeaderLevel(int headerLevel) {
	getLabel().setHeaderLevel(headerLevel);
    }
}
