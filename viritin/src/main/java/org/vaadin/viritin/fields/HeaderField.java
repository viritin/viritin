package org.vaadin.viritin.fields;

import org.vaadin.viritin.label.Header;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

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
public class HeaderField<T> extends LabelField<T>
        {
    
    private static final long serialVersionUID = -6461526936461789654L;

    public HeaderField(String caption) {
        super(caption);
    }

    private CaptionGenerator<T> captionGenerator = new ToStringCaptionGenerator<>();
    private Header header = new Header("");

    public HeaderField() {
    }

    @Override
    protected Component initContent() {
        updateLabel();

        return header;
    }

    @Override
    protected void updateLabel() {
        String caption;
        if (captionGenerator != null) {
            caption = captionGenerator.getCaption(getValue());
        } else {
            caption = getValue().toString();
        }
        header.setValue(caption);
    }

    public Label getHeader() {
        return header;
    }

    public void setHeaderLevel(int headerLevel) {
	header.setHeaderLevel(headerLevel);
    }
    
}
