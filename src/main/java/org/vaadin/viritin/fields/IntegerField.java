package org.vaadin.viritin.fields;

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

    protected void userInputToValue(String str) {
        setValue(Integer.parseInt(str));
    }

    @Override
    public Class<? extends Integer> getType() {
        return Integer.class;
    }

    public IntegerField withCaption(String caption) {
        setCaption(caption);
        return this;
    }


}
