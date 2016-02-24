package org.vaadin.viritin.fields;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import org.vaadin.viritin.label.RichText;

/**
 * A field which represents the value always in a label, so it is not editable.
 * <p>
 * The use case is a non editable value in a form, e.g. an id.
 * <p>
 * The creation of the label text can be controlled via a
 * {@link org.vaadin.viritin.fields.CaptionGenerator}.
 * 
 * @author Daniel Nordhoff-Vergien
 *
 * @param <T>
 *            the type of the entity
 */
public class LabelField<T> extends CustomField<T> {
    private final Class<T> type;

    private static class ToStringCaptionGenerator<T> implements
            CaptionGenerator<T> {

        @Override
        public String getCaption(T option) {
            if (option == null) {
                return "";
            } else {
                return option.toString();
            }
        }
    }

    public LabelField(Class<T> type, String caption) {
        super();
        this.type = type;
        setCaption(caption);
    }

    private CaptionGenerator<T> captionGenerator = new ToStringCaptionGenerator<T>();
    private Label label = new RichText();

    public LabelField(Class<T> type) {
        super();
        this.type = type;
    }

    public LabelField() {
        this.type = (Class<T>) String.class;
    }

    public LabelField<T> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public LabelField<T> withValue(T newFieldValue) {
        setValue(newFieldValue);
        return this;
    }
    
    @Override
    protected Component initContent() {
        updateLabel();

        return label;
    }

    protected void updateLabel() {
        String caption;
        if (captionGenerator != null) {
            caption = captionGenerator.getCaption(getValue());
        } else {
            caption = getValue().toString();
        }
        label.setValue(caption);
    }

    @Override
    protected void setInternalValue(T newValue) {
        super.setInternalValue(newValue);
        updateLabel();
    }

    @Override
    public Class<? extends T> getType() {
        return type;
    }

    /**
     * Sets the CaptionGenerator for creating the label value.
     * 
     * @param captionGenerator the caption generator used to format the displayed property
     */
    public void setCaptionGenerator(CaptionGenerator<T> captionGenerator) {
        this.captionGenerator = captionGenerator;
        updateLabel();
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
    }
    
}