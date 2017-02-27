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
package org.vaadin.viritin.v7.fluency.data;

import org.vaadin.viritin.fluency.ui.FluentComponent;

import com.vaadin.v7.data.Property;

/**
 * A {@link Property} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @param <T> type of values of the property
 * @see Property
 */
public interface FluentProperty<S extends FluentComponent<S> & FluentProperty<S, T>, T>
        extends Property<T> {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the value of the Property.
     * <p>
     * Implementing this functionality is optional. If the functionality is
     * missing, one should declare the Property to be in read-only mode and
     * throw <code>Property.ReadOnlyException</code> in this function.
     * </p>
     *
     * Note : Since Vaadin 7.0, setting the value of a non-String property as a
     * String is no longer supported.
     *
     * @param newValue New value of the Property. This should be assignable to
     * the type returned by getType
     *
     * @throws Property.ReadOnlyException if the object is in read-only mode
     * @return this (for method chaining)
     * @see #setValue(java.lang.Object)
     */
    public S withValue(T newValue) throws Property.ReadOnlyException;

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the Property's read-only mode to the specified status.
     *
     * This functionality is optional, but all properties must implement the
     * <code>isReadOnly</code> mode query correctly.
     *
     * @param newStatus new read-only status of the Property
     * @return this (for method chaining)
     * @see #setReadOnly(boolean)
     */
    public S withReadOnly(boolean newStatus);

    /**
     * A {@link ValueChangeNotifier} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see ValueChangeNotifier
     */
    public interface FluentValueChangeNotifier<S extends FluentValueChangeNotifier<S>> 
            extends ValueChangeNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Registers a new value change listener for this Property.
         *
         * @param listener the new Listener to be registered
         * @return this (for method chaining)
         */
        public S withValueChangeListener(Property.ValueChangeListener listener);

    }
    
    /**
     * A {@link Editor} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Editor
     */
    public interface FluentEditor<S extends FluentEditor<S>>
            extends Editor, FluentViewer<S> {
        
    }

    /**
     * A {@link Viewer} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Viewer
     */
    public interface FluentViewer<S extends FluentViewer<S>> extends Viewer {

        /**
         * Sets the Property that serves as the data source of the viewer.
         *
         * @param newDataSource the new data source Property
         * @return this (for method chaining)
         * @see #setPropertyDataSource(com.vaadin.data.Property) 
         */
        @SuppressWarnings("rawtypes")
        public S withPropertyDataSource(Property newDataSource);

    }

    /**
     * A {@link ReadOnlyStatusChangeNotifier} complemented by fluent
     * setters.
     *
     * @param <S> Self-referential generic type
     * @see ReadOnlyStatusChangeNotifier
     */
    public interface FluentReadOnlyStatusChangeNotifier<S extends FluentReadOnlyStatusChangeNotifier<S>> 
            extends ReadOnlyStatusChangeNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Registers a new read-only status change listener for this Property.
         *
         * @param listener the new Listener to be registered
         * @return this (for method chaining)
         * @see
         * #addReadOnlyStatusChangeListener(com.vaadin.data.Property.ReadOnlyStatusChangeListener)
         */
        public S withReadOnlyStatusChangeListener(
                Property.ReadOnlyStatusChangeListener listener);

    }

}
