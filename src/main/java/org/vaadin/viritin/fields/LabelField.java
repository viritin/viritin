package org.vaadin.viritin.fields;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.v7.fields.CaptionGenerator;

/**
 * A field which represents the value always in a label, so it is not editable.
 * <p>
 * The use case is a non editable value in a form, e.g. an id.
 * <p>
 * The creation of the label text can be controlled via a
 * {@link org.vaadin.viritin.v7.fields.CaptionGenerator}.
 *
 * @author Daniel Nordhoff-Vergien
 *
 * @param <T> the type of the entity
 */
public class LabelField<T> extends CustomField<T> {

    private static final long serialVersionUID = -3079451926367430515L;

    private T value;

    @Override
    protected void doSetValue(T value) {
        this.value = value;
        updateLabel();
    }

    @Override
    public T getValue() {
        return value;
    }

    private static class ToStringCaptionGenerator<T> implements
            CaptionGenerator<T> {

        private static final long serialVersionUID = 1149675718238329960L;

        @Override
        public String getCaption(T option) {
            if (option == null) {
                return "";
            } else {
                return option.toString();
            }
        }
    }

    public LabelField(String caption) {
        super();
        setCaption(caption);
    }

    private CaptionGenerator<T> captionGenerator = new ToStringCaptionGenerator<>();
    private Label label = new RichText();

    public LabelField() {
    }

    public LabelField<T> withFullWidth() {
        setWidth("100%");
        return this;
    }

    public LabelField<T> withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public LabelField<T> withWidth(String width) {
        setWidth(width);
        return this;
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

    /**
     * Sets the CaptionGenerator for creating the label value.
     *
     * @param captionGenerator the caption generator used to format the
     * displayed property
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
