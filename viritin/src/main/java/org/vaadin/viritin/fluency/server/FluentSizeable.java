/*
 * Copyright 2016 Matti Tahvonen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.viritin.fluency.server;

import org.vaadin.viritin.MSize;

import com.vaadin.server.Sizeable;

/**
 * A {@link Sizeable} complemented by fluent setters.
 * 
 * @author Max Schuster
 * @param <S> Self-referential generic typeS
 * @see Sizeable
 */
public interface FluentSizeable<S extends FluentSizeable<S>>
        extends Sizeable {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the height of the component using String presentation.
     *
     * String presentation is similar to what is used in Cascading Style Sheets.
     * Size can be length or percentage of available size.
     *
     * The empty string ("") or null will unset the height and set the units to
     * pixels.
     *
     * See <a
     * href="http://www.w3.org/TR/REC-CSS2/syndata.html#value-def-length">CSS
     * specification</a> for more details.
     *
     * @param height in CSS style string representation
     * @return this (for method chaining)
     * @see #setHeight(java.lang.String)
     */
    public default S withHeight(String height) {
        ((Sizeable) this).setHeight(height);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the height of the object. Negative number implies unspecified size
     * (terminal is free to set the size).
     *
     * @param height the height of the object.
     * @param unit the unit used for the width.
     * @return this (for method chaining)
     * @see #setHeight(float, com.vaadin.server.Sizeable.Unit)
     */
    public default S withHeight(float height, Unit unit) {
        ((Sizeable) this).setHeight(height, unit);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the width of the component using String presentation.
     *
     * String presentation is similar to what is used in Cascading Style Sheets.
     * Size can be length or percentage of available size.
     *
     * The empty string ("") or null will unset the width and set the units to
     * pixels.
     *
     * See <a
     * href="http://www.w3.org/TR/REC-CSS2/syndata.html#value-def-length">CSS
     * specification</a> for more details.
     *
     * @param width in CSS style string representation, null or empty string to
     * reset
     * @return this (for method chaining)
     * @see #setWidth(java.lang.String)
     */
    public default S withWidth(String width) {
        ((Sizeable) this).setWidth(width);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the width of the object. Negative number implies unspecified size
     * (terminal is free to set the size).
     *
     * @param width the width of the object.
     * @param unit the unit used for the width.
     * @return this (for method chaining)
     * @see #setWidth(float, com.vaadin.server.Sizeable.Unit)
     */
    public default S withWidth(float width, Unit unit) {
        ((Sizeable) this).setWidth(width, unit);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the size to 100% x 100%.
     * @return this (for method chaining)
     * @see #setSizeFull()
     */
    public default S withFullSize() {
        ((Sizeable) this).setSizeFull();
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the width to 100%.
     *
     * @return this (for method chaining)
     */
    public default S withFullWidth() {
        return withWidth("100%");
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the height to 100%.
     *
     * @return this (for method chaining)
     */
    public default S withFullHeight() {
        return withHeight("100%");
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Clears any size settings.
     * @return this (for method chaining)
     * @see #setSizeUndefined()
     */
    public default S withUndefinedSize() {
        ((Sizeable) this).setSizeUndefined();
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Clears any defined width
     *
     * @return this (for method chaining)
     * @see #setWidthUndefined()
     */
    public default S withUndefinedWidth() {
        ((Sizeable) this).setWidthUndefined();
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Clears any defined height
     *
     * @return this (for method chaining)
     * @see #setHeightUndefined()
     */
    public default S withUndefinedHeight() {
        ((Sizeable) this).setHeightUndefined();
        return (S) this;
    }

    public default S withSize(String width, String height) {
        setWidth(width);
        setHeight(height);
        return (S) this;
    }

    public default S withSize(MSize mSize) {
        setWidth(mSize.getWidth(), mSize.getWidthUnit());
        setHeight(mSize.getHeight(), mSize.getHeightUnit());
        return (S) this;
    }

}
