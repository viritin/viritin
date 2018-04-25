package org.vaadin.viritin.fluency.ui;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;

/**
 * The base interface for fluent versions of {@link AbstractOrderedLayout}
 *
 * @author Daniel Nordhoff-Vergien
 * @see AbstractOrderedLayout
 */
public interface FluentAbstractOrderedLayout<S extends FluentAbstractLayout<S> & FluentLayout.FluentAlignmentHandler<S> & FluentLayout.FluentMarginHandler<S> & FluentLayout.FluentSpacingHandler<S>>
        extends FluentLayout.FluentAlignmentHandler<S>,
        FluentLayout.FluentMarginHandler<S>,
        FluentLayout.FluentSpacingHandler<S>, FluentAbstractLayout<S> {
    // Javadoc copied form Vaadin Framework
    /**
     * Adds a component into indexed position in this container.
     *
     * @param c
     *            the component to be added.
     * @param index
     *            the index of the component position. The components currently
     *            in and after the position are shifted forwards.
     * @return this (for method chaining)
     * @see AbstractOrderedLayout#addComponent(Component, int)
     */
    public default S withComponent(Component c, int index) {
        ((AbstractOrderedLayout) this).addComponent(c, index);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a component into this container. The component is added to the left
     * or on top of the other components.
     *
     * @param c
     *            the component to be added.
     * @return this (for method chaining)
     * @see AbstractOrderedLayout#addComponentAsFirst(Component)
     */
    public default S withComponentAsFirst(Component c) {
        ((AbstractOrderedLayout) this).addComponentAsFirst(c);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * <p>
     * This method is used to control how excess space in layout is distributed
     * among components. Excess space may exist if layout is sized and contained
     * non relatively sized components don't consume all available space.
     * 
     * <p>
     * Example how to distribute 1:3 (33%) for component1 and 2:3 (67%) for
     * component2 :
     * 
     * <code>
     * layout.setExpandRatio(component1, 1);<br>
     * layout.setExpandRatio(component2, 2);
     * </code>
     * 
     * <p>
     * If no ratios have been set, the excess space is distributed evenly among
     * all components.
     * 
     * <p>
     * Note, that width or height (depending on orientation) needs to be defined
     * for this method to have any effect.
     *
     * @see Sizeable
     * 
     * @param component
     *            the component in this layout which expand ratio is to be set
     * @param ratio
     *            new expand ratio (greater or equal to 0)
     * 
     * @return this (for method chaining)
     * @see AbstractOrderedLayout#setExpandRatio(Component, float)
     * @throws IllegalArgumentException
     *             if the expand ratio is negative or the component is not a
     *             direct child of the layout
     */
    public default S withExpandRatio(Component component, float ratio) {
        ((AbstractOrderedLayout) this).setExpandRatio(component, ratio);
        return (S) this;
    }
}
