package org.vaadin.viritin.fluency.ui;

import com.vaadin.data.HasValue;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.AbstractTextField;

public interface FluentTextField<S extends FluentTextField<S>>
        extends FluentAbstractField<S, String>, FluentHasValueChangeMode<S> {

    /**
     * @deprecated Use withPlaceholder instead
     * @param inputPrompt the input prompt
     * @return this for method chaining
     */
    public default S withInputPrompt(String inputPrompt) {
        ((AbstractTextField) this).setPlaceholder(inputPrompt);
        return (S) this;
    }

    /**
     * Sets the placeholder text for the field.
     *
     * @see AbstractTextField#setPlaceholder(java.lang.String) 
     * @param placeholder the placeholder text to be used
     * @return this for method chaining
     */
    public default S withPlaceholder(String placeholder) {
        ((AbstractTextField) this).setPlaceholder(placeholder);
        return (S) this;
    }
    
    public default S addTextChangeListener(HasValue.ValueChangeListener<String> l) {
        ((AbstractTextField) this).addValueChangeListener(l);
        ((AbstractTextField) this).setValueChangeMode(ValueChangeMode.LAZY);
        return (S) this;
    }

}
