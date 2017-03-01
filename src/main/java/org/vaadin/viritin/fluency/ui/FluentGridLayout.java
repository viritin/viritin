package org.vaadin.viritin.fluency.ui;

import com.vaadin.ui.GridLayout;

/**
 * The base interface for fluent versions of {@link GridLayout}
 * 
 * @author Daniel Nordhoff-Vergien
 * @see GridLayout
 */
public interface FluentGridLayout<S extends FluentGridLayout<S>>
        extends FluentAbstractLayout<S>, FluentLayout.FluentAlignmentHandler<S>,
        FluentLayout.FluentMarginHandler<S>,
        FluentLayout.FluentSpacingHandler<S> {

}
