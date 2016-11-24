package org.vaadin.viritin.fluency.ui;

import org.vaadin.viritin.fluency.event.FluentAction.FluentNotifier;
import org.vaadin.viritin.fluency.server.FluentScrollable;

public interface FluentPanel<S extends FluentPanel<S>>
        extends FluentScrollable<S>, FluentComponent.FluentFocusable<S>,
        FluentNotifier<S>, FluentComponent<S>,
        FluentSingleComponentContainer<S> {

}
