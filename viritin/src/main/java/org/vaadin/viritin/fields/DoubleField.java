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

	private String step = "any";
	private String definedStep;

    public DoubleField() {
        setSizeUndefined();
    }

    public DoubleField(String caption) {
        setSizeUndefined();
        setCaption(caption);
    }

    @Override
    protected void userInputToValue(String str) {
        if (StringUtils.isNotBlank(str)) {
            // a hacky support for locales that use comma as a decimal separator
            // some browser do the conversion already on the browser, IE don't
            str = str.replaceAll(",", ".");
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

	public DoubleField withStep(String step) {
		this.definedStep = step;
		return this;
	}

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    protected void configureHtmlElement() {
		if (StringUtils.isNotBlank(definedStep)) {
			step = definedStep;

		} else if (getValue() != null) {
			String s = String.valueOf(getValue()).replaceAll("\\d", "0").replaceAll("(\\d{1})$", "1");
			if ("any".equals(step) || s.length() > step.length()) {
				step = s;
			}
		}
		s.setProperty("step", step);

        s.setProperty("type", getHtmlFieldType());
        // prevent all but numbers or single dot after a number with a simple js
        s.setJavaScriptEventHandler("keypress",
                "function(e) {if(e.metaKey || e.ctrlKey) return true; var c = viritin.getChar(e); if( (c === '.' || c === ',') && this.value != '' && this.value.indexOf('.') == -1 && this.value.indexOf(',') == -1) return true; return c==null || /^[-\\d\\n\\t\\r]+$/.test(c);}");
        }

}
