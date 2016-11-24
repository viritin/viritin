package org.vaadin.viritin.fields;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.viritin.util.HtmlElementPropertySetter;

/**
 *
 * @author Matti Tahvonen
 * @param <T>  field value type
 */
public abstract class AbstractNumberField<T> extends CustomField<T> implements
        EagerValidateable, FieldEvents.TextChangeNotifier {

    private String htmlFieldType = "number";

    protected MTextField tf = new MTextField() {
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
        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            Object value = event.getProperty().getValue();
            if(value != null) {
                userInputToValue(String.valueOf(value));
            }else {
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

}
