package org.vaadin.viritin.ui;

import org.vaadin.viritin.fluency.event.FluentAction.FluentNotifier;
import org.vaadin.viritin.fluency.server.FluentScrollable;
import org.vaadin.viritin.fluency.ui.FluentAbstractSingleComponentContainer;
import org.vaadin.viritin.fluency.ui.FluentComponent;

public interface FluentPanel<S extends FluentPanel<S>>
        extends FluentAbstractSingleComponentContainer<S>, FluentScrollable<S>,
        FluentComponent.FluentFocusable<S>, FluentNotifier<S> {
}
