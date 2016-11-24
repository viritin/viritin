package org.vaadin.viritin.fluency.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.SingleComponentContainer;

public interface FluentSingleComponentContainer<S extends FluentSingleComponentContainer<S>>
        extends SingleComponentContainer, FluentHasComponents<S>,
        FluentHasComponents.FluentComponentAttachDetachNotifier<S> {
    // Javadoc copied form Vaadin Framework
    /**
     * Sets the content of this container. The content is a component that
     * serves as the outermost item of the visual contents.
     * 
     * The content should always be set, either as a constructor parameter or by
     * calling this method.
     * 
     * @param content
     *            a component (typically a layout) to use as content
     * @return this (for method chaining)
     * @see SingleComponentContainer#addComponent(com.vaadin.ui.Component)
     */
    public S withContent(Component content);
}