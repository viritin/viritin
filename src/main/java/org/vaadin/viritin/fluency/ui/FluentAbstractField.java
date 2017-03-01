package org.vaadin.viritin.fluency.ui;

import org.vaadin.viritin.fluency.ui.FluentComponent.FluentFocusable;

public interface FluentAbstractField<S extends FluentAbstractField<S, T>, T>
        extends FluentComponent<S>, FluentHasValue<S, T>, FluentFocusable<S> {

}
