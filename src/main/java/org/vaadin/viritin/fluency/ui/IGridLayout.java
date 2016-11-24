package org.vaadin.viritin.fluency.ui;

import com.vaadin.ui.GridLayout;

/**
 * The base interface for fluent versions of {@link GridLayout}
 * 
 * @author Daniel Nordhoff-Vergien
 * @see GridLayout
 */
public interface IGridLayout extends FluentAbstractLayout<IGridLayout>,
        FluentLayout.FluentAlignmentHandler<IGridLayout>,
        FluentLayout.FluentMarginHandler<IGridLayout>,
        FluentLayout.FluentSpacingHandler<IGridLayout> {

}
