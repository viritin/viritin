package org.vaadin.viritin.fluency.server;

import com.vaadin.server.Scrollable;

/**
 * A {@link Scrollable} complemented by fluent setters.
 * 
 * @author Daniel Nordhoff-Vergien
 * @param <S>
 *            Self-referential generic type S
 * @see Scrollable
 */
public interface FluentScrollable<S extends FluentScrollable<S>>
        extends Scrollable {
    // Javadoc copied form Vaadin Framework
    /**
     * Sets scroll left offset.
     * 
     * <p>
     * Scrolling offset is the number of pixels this scrollable has been
     * scrolled right.
     * </p>
     * 
     * @param scrollLeft
     *            the xOffset.
     * @return this (for method chaining)
     * @see Scrollable#setScrollLeft(int)
     */
    public default S withScrollLeft(int scrollLeft) {
        ((Scrollable) this).setScrollLeft(scrollLeft);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets scroll top offset.
     * 
     * <p>
     * Scrolling offset is the number of pixels this scrollable has been
     * scrolled down.
     * </p>
     * 
     * <p>
     * The scrolling position is limited by the current height of the content
     * area. If the position is below the height, it is scrolled to the bottom.
     * However, if the same response also adds height to the content area,
     * scrolling to bottom only scrolls to the bottom of the previous content
     * area.
     * </p>
     * 
     * @param scrollTop
     *            the yOffset.
     * @return this (for method chaining)
     * @see Scrollable#setScrollTop(int)
     */
    public default S withScrollTop(int scrollTop) {
        ((Scrollable) this).setScrollTop(scrollTop);
        return (S) this;
    }
}
