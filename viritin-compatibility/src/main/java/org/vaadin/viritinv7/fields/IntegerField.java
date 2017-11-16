package org.vaadin.viritinv7.fields;

import com.vaadin.event.FieldEvents;
import org.apache.commons.lang3.StringUtils;

/**
 * An field to edit integers.
 *
 * The actual field implementation uses HTML5 input type "number" so you'll have
 * up/down arrows available in addition to simply typing the value. The client
 * side also prevent non numeric characters to be typed. Some of these advanced
 * UX helpers may work in modern browsers only, but the field should be
 * perfectly usable with older browsers as well.
 *
 * @author Matti Tahvonen
 */
public class IntegerField extends AbstractNumberField<Integer> {

    private static final long serialVersionUID = 377246000306551089L;

    public IntegerField() {
        setSizeUndefined();
    }

    public IntegerField(String caption) {
        setCaption(caption);
    }

    @Override
    protected void userInputToValue(String str) {
        if (StringUtils.isNotBlank(str)) {
            setValue(Integer.parseInt(str));
        } else {
            setValue(null);
        }
    }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

    public IntegerField withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public IntegerField withId(String id) {
        setId(id);
        return this;
    }

    public IntegerField withFullWidth() {
        setWidth("100%");
        return this;
    }

    public IntegerField withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public IntegerField withWidth(String width) {
        setWidth(width);
        return this;
    }

    @Override
    public IntegerField withBlurListener(FieldEvents.BlurListener listener) {
        return (IntegerField) super.withBlurListener(listener);
    }

    @Override
    public IntegerField withFocusListener(FieldEvents.FocusListener listener) {
        return (IntegerField) super.withFocusListener(listener);
    }
}
