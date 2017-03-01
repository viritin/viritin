package org.vaadin.viritin.fluency.ui;

import com.vaadin.ui.CustomField;

public interface FluentCustomField<S extends FluentCustomField<S, T>, T>
        extends FluentAbstractField<S, T>, FluentHasComponents<S> {
    /**
     * Sets the component to which all methods from the {@link Focusable}
     * interface should be delegated.
     * <p>
     * Set this to a wrapped field to include that field in the tabbing order,
     * to make it receive focus when {@link #focus()} is called and to make it
     * be correctly focused when used as a Grid editor component.
     * <p>
     * By default, {@link Focusable} events are handled by the super class and
     * ultimately ignored.
     *
     * @param focusDelegate
     *            the focusable component to which focus events are redirected
     */
    public default S withFocusDelegate(Focusable focusDelegate) {
        ((CustomField<T>) this).setFocusDelegate(focusDelegate);
        return (S) this;
    }
}
