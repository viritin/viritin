package org.vaadin.viritin.fields;

import org.vaadin.viritin.fluency.ui.FluentCustomField;
import org.vaadin.viritin.util.HtmlElementPropertySetter;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;

/**
 * @param <T> field value type
 * @author Matti Tahvonen
 */
public abstract class AbstractNumberField<S extends AbstractNumberField<S, T>, T>
        extends CustomField<T>
        implements FieldEvents.FocusNotifier, FieldEvents.BlurNotifier,
        FluentCustomField<S, T> {

    private static final long serialVersionUID = 5925606478174987241L;

    private String htmlFieldType = "number";

    protected T value;

    protected TextField tf = new TextField() {

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

    private boolean ignoreValueChange = false;

    protected HtmlElementPropertySetter s = new HtmlElementPropertySetter(tf);
    protected ValueChangeListener<String> vcl = new ValueChangeListener<String>() {

        private static final long serialVersionUID = 5034199201545161061L;

        @Override
        public void valueChange(ValueChangeEvent<String> event) {
            if (!ignoreValueChange) {
                T old = getValue();
                String value = event.getValue();
                if (value != null) {
                    userInputToValue(value);
                    fireEvent(new ValueChangeEvent(AbstractNumberField.this, old, true));
                } else {
                    setValue(null);
                }
            }
        }

    };

    public AbstractNumberField() {
        tf.addValueChangeListener(vcl);
    }

    protected abstract void userInputToValue(String str);

    @Override
    protected Component initContent() {
        return tf;
    }

    @Override
    protected void doSetValue(T value) {
        this.value = value;
        ignoreValueChange = true;
        if (value == null) {
            tf.clear();
        } else {
            tf.setValue(valueToPresentation(value));
        }
        ignoreValueChange = false;
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
    public Registration addBlurListener(BlurListener listener) {
        return tf.addBlurListener(listener);
    }

    @Override
    public Registration addFocusListener(FocusListener listener) {
        return tf.addFocusListener(listener);
    }

    /**
     * Adds a BlurListener to the Component which gets fired when a Field loses
     * keyboard focus, returning this instance in a fluent fashion.
     *
     * @param listener the listener to be added
     * @return this instance
     */
    public AbstractNumberField<S, T> withBlurListener(BlurListener listener) {
        addBlurListener(listener);
        return this;
    }

    /**
     * Adds a FocusListener to the Component which gets fired when a Field
     * receives keyboard focus, returning this instance in a fluent fashion.
     *
     * @param listener the listener to be added
     * @return this instance
     */
    public AbstractNumberField<S, T> withFocusListener(FocusListener listener) {
        addFocusListener(listener);
        return this;
    }

    public S withPlaceHolder(String placeHolderText) {
        tf.setPlaceholder(placeHolderText);
        return (S) this;
    }

}
