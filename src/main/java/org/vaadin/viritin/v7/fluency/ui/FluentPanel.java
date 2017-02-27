package org.vaadin.viritin.v7.fluency.ui;

import org.vaadin.viritin.fluency.server.FluentScrollable;
import org.vaadin.viritin.fluency.ui.FluentComponent;
import org.vaadin.viritin.v7.fluency.event.FluentAction.FluentNotifier;

public interface FluentPanel<S extends FluentPanel<S>>
        extends FluentScrollable<S>, FluentComponent.FluentFocusable<S>,
        FluentNotifier<S>, FluentComponent<S>,
        FluentSingleComponentContainer<S> {

}
