package org.vaadin.viritin.fluency.ui;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.HasValueChangeMode;

public interface FluentHasValueChangeMode<S extends FluentHasValueChangeMode<S>>
        extends HasValueChangeMode, FluentComponent<S> {
    /**
     * Sets the mode how the TextField triggers {@link ValueChangeEvent}s.
     *
     * @param valueChangeMode
     *            the new mode
     *
     * @see ValueChangeMode
     */
    public default S withValueChangeMode(ValueChangeMode valueChangeMode) {
        ((HasValueChangeMode) this).setValueChangeMode(valueChangeMode);
        return (S) this;
    }

    /**
     * Sets how often {@link ValueChangeEvent}s are triggered when the
     * {@link ValueChangeMode} is set to either {@link ValueChangeMode#LAZY} or
     * {@link ValueChangeMode#TIMEOUT}.
     *
     * @param valueChangeTimeout
     *            timeout in milliseconds, must be greater or equal to 0
     * @throws IllegalArgumentException
     *             if given timeout is smaller than 0
     *
     * @see ValueChangeMode
     */
    public default S withValueChangeTimeout(int valueChangeTimeout) {
        ((HasValueChangeMode) this).setValueChangeTimeout(valueChangeTimeout);
        return (S) this;
    }
}
