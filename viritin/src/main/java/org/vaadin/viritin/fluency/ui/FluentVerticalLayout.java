package org.vaadin.viritin.fluency.ui;

import com.vaadin.ui.VerticalLayout;

/**
 * The base interface for fluent versions of {@link VerticalLayout}
 * 
 * @author Daniel Nordhoff-Vergien
 * @see VerticalLayout
 */
public interface FluentVerticalLayout<S extends FluentVerticalLayout<S>>
        extends FluentLayout<S>, FluentAbstractOrderedLayout<S> {
}
