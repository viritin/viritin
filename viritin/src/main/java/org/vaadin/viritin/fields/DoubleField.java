package org.vaadin.viritin.fields;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.event.FieldEvents;

/**
 * An field to edit Double values.
 *
 * The actual field implementation uses HTML5 input type "number" so you'll have
 * up/down arrows available in addition to simply typing the value. The client
 * side also prevent non numeric characters to be typed. Some of these advanced
 * UX helpers may work in modern browsers only, but the field should be
 * perfectly usable with older browsers as well.
 *
 * @author Matti Tahvonen
 */
public class DoubleField extends AbstractNumberField<DoubleField, Double> {

    private static final long serialVersionUID = 377246000306551089L;

    public DoubleField() {
        setSizeUndefined();
    }

    public DoubleField(String caption) {
        setCaption(caption);
    }

    @Override
    protected void userInputToValue(String str) {
        if (StringUtils.isNotBlank(str)) {
            value = Double.parseDouble(str);
        } else {
            value = null;
        }
    }

    @Override
    public DoubleField withBlurListener(FieldEvents.BlurListener listener) {
        return (DoubleField) super.withBlurListener(listener);
    }

    @Override
    public DoubleField withFocusListener(FieldEvents.FocusListener listener) {
        return (DoubleField) super.withFocusListener(listener);
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    protected void configureHtmlElement() {
        s.setProperty("type", getHtmlFieldType());
        // prevent all but numbers or single dot after a number with a simple js
        s.setJavaScriptEventHandler("keypress",
                "function(e) {if(e.metaKey || e.ctrlKey) return true; var c = viritin.getChar(e); if(c === '.' && this.value != '' && this.value.indexOf('.') == -1) return true; return c==null || /^[-\\d\\n\\t\\r]+$/.test(c);}");
        }

}
