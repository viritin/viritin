package org.vaadin.viritin.fields;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.event.FieldEvents;

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
public class IntegerField extends AbstractNumberField<IntegerField, Integer> {

    private static final long serialVersionUID = 377246000306551089L;
    private Integer value;

    public IntegerField() {
        setSizeUndefined();
    }

    public IntegerField(String caption) {
        setCaption(caption);
    }

    @Override
    protected void userInputToValue(String str) {
        if (StringUtils.isNotBlank(str)) {
            value = Integer.parseInt(str);
        } else {
            value = null;
        }
    }

    @Override
    public IntegerField withBlurListener(FieldEvents.BlurListener listener) {
        return (IntegerField) super.withBlurListener(listener);
    }

    @Override
    public IntegerField withFocusListener(FieldEvents.FocusListener listener) {
        return (IntegerField) super.withFocusListener(listener);
    }

    @Override
    public Integer getValue() {
        return value;
    }

}
