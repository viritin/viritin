package org.vaadin.viritin.fields;

import org.vaadin.viritin.util.HtmlElementPropertySetter;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;

/**
 * @param <T> field value type
 * @author Matti Tahvonen
 */
public abstract class AbstractNumberField<T> extends CustomField<T> implements
        EagerValidateable, FieldEvents.TextChangeNotifier, FieldEvents.FocusNotifier, FieldEvents.BlurNotifier {

    private static final long serialVersionUID = 5925606478174987241L;

    private String htmlFieldType = "number";

    protected MTextField tf = new MTextField() {

        private static final long serialVersionUID = 6823601969399906594L;

        @Override
        public void beforeClientResponse(boolean initial) {
            super.beforeClientResponse(initial);
            configureHtmlElement();
        }
    };

    protected void configureHtmlElement() {
        s.setProperty("type", getHtmlFieldType());
        // prevent all but numbers with a simple js
        s.setJavaScriptEventHandler("keypress",
                "function(e) {var c = viritin.getChar(e); return c==null || /^[-\\d\\n\\t\\r]+$/.test(c);}");
    }

    protected HtmlElementPropertySetter s = new HtmlElementPropertySetter(tf);
    protected Property.ValueChangeListener vcl = new Property.ValueChangeListener() {

        private static final long serialVersionUID = 5034199201545161061L;

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            Object value = event.getProperty().getValue();
            if (value != null) {
                userInputToValue(String.valueOf(value));
            } else {
                setValue(null);
            }
        }
    };

    protected FieldEvents.TextChangeListener tcl;

    protected abstract void userInputToValue(String str);

    @Override
    protected Component initContent() {
        tf.addValueChangeListener(vcl);
        return tf;
    }

    @Override
    protected void setInternalValue(T newValue) {
        super.setInternalValue(newValue);
        if (newValue == null) {
            tf.setValue(null);
        } else {
            tf.setValue(valueToPresentation(newValue));
        }
    }

    protected String valueToPresentation(T newValue) {
        return newValue.toString();
    }

    public String getHtmlFieldType() {
        return htmlFieldType;
    }

    /**
     * Sets the type property of the input field used on the browser. "number"
     * by default.
     *
     * @param htmlFieldType the type value
     */
    public void setHtmlFieldType(String htmlFieldType) {
        this.htmlFieldType = htmlFieldType;
    }

    @Override
    public void addTextChangeListener(
            FieldEvents.TextChangeListener listener) {
        tf.addTextChangeListener(listener);
    }

    @Override
    public void addListener(
            FieldEvents.TextChangeListener listener) {
        tf.addTextChangeListener(listener);
    }

    @Override
    public void removeTextChangeListener(
            FieldEvents.TextChangeListener listener) {
        tf.removeTextChangeListener(listener);
    }

    @Override
    public void removeListener(
            FieldEvents.TextChangeListener listener) {
        tf.removeTextChangeListener(listener);
    }

    @Override
    public boolean isEagerValidation() {
        return tf.isEagerValidation();
    }

    @Override
    public void setEagerValidation(boolean eagerValidation) {
        tf.setEagerValidation(true);
        if (eagerValidation && tcl == null) {
            tcl = new FieldEvents.TextChangeListener() {

                private static final long serialVersionUID = 2244473923631502546L;

                @Override
                public void textChange(
                        FieldEvents.TextChangeEvent event) {
                    userInputToValue(event.getText());
                }
            };
            tf.addTextChangeListener(tcl);
        }
        if (!eagerValidation && tcl != null) {
            tf.removeTextChangeListener(tcl);
            tcl = null;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        tf.setEnabled(enabled);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        tf.setReadOnly(readOnly);
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);
        if (tf != null) {
            if (width != -1) {
                tf.setWidth("100%");
            } else {
                tf.setWidth(null);
            }
        }
    }

    @Override
    public void addBlurListener(FieldEvents.BlurListener listener) {
        tf.addBlurListener(listener);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addListener(FieldEvents.BlurListener listener) {
        addBlurListener(listener);
    }

    @Override
    public void removeBlurListener(FieldEvents.BlurListener listener) {
        tf.removeBlurListener(listener);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeListener(FieldEvents.BlurListener listener) {
        removeBlurListener(listener);
    }

    @Override
    public void addFocusListener(FieldEvents.FocusListener listener) {
        tf.addFocusListener(listener);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addListener(FieldEvents.FocusListener listener) {
        addFocusListener(listener);
    }

    @Override
    public void removeFocusListener(FieldEvents.FocusListener listener) {
        tf.removeFocusListener(listener);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeListener(FieldEvents.FocusListener listener) {
        removeFocusListener(listener);
    }

    /**
     * Adds a BlurListener to the Component which gets fired when a Field loses keyboard focus, returning
     * this instance in a fluent fashion.
     *
     * @param listener
     * @return this instance
     */
    public AbstractNumberField<T> withBlurListener(FieldEvents.BlurListener listener) {
        addBlurListener(listener);
        return this;
    }

    /**
     * Adds a FocusListener to the Component which gets fired when a Field receives keyboard focus, returning
     * this instance in a fluent fashion.
     *
     * @param listener
     * @return this instance
     */
    public AbstractNumberField<T> withFocusListener(FieldEvents.FocusListener listener) {
        addFocusListener(listener);
        return this;
    }

}
