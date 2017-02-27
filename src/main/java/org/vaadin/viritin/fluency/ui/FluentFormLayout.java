package org.vaadin.viritin.fluency.ui;

import com.vaadin.ui.FormLayout;

/**
 * The base interface for fluent versions of {@link FormLayout}
 * 
 * @author Daniel Nordhoff-Vergien
 * @see FormLayout
 */
public interface FluentFormLayout<S extends FluentFormLayout<S>>
        extends FluentLayout<S>, FluentAbstractOrderedLayout<S> {
}
