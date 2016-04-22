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
package org.vaadin.viritin.fluency.data;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;

/**
 * A {@link Container} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see Container
 */
public interface FluentContainer<S extends FluentContainer<S>>
        extends Container {

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a new Property to all Items in the Container. The Property ID, data
     * type and default value of the new Property are given as parameters.
     * <p>
     * This functionality is optional.
     *
     * @param propertyId ID of the Property
     * @param type Data type of the new Property
     * @param defaultValue The value all created Properties are initialized to
     * @return this (for method chaining)
     * @throws UnsupportedOperationException if the container does not support
     * explicitly adding container properties
     * @see #withContainerProperty(java.lang.Object, java.lang.Class,
     * java.lang.Object)
     */
    public S withContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue) throws UnsupportedOperationException;

    /**
     * An {@link Ordered} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Ordered
     */
    public interface FluentOrdered<S extends FluentOrdered<S>> extends Ordered {

        // no methods to override
        
    }

    /**
     * A {@link Sortable} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Sortable
     */
    public interface FluentSortable<S extends FluentSortable<S>>
            extends Sortable {

        // no methods to override
        
    }

    /**
     * An {@link Indexed} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Indexed
     */
    public interface FluentIndexed<S extends FluentIndexed<S>>
            extends Indexed, FluentOrdered<S> {
        
        // no methods to override
        
    }

    /**
     * A {@link Hierarchical} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Hierarchical
     */
    public interface FluentHierarchical<S extends FluentHierarchical<S>>
            extends Hierarchical, FluentContainer<S> {
        
        // no methods to override
        
    }

    /**
     * A {@link SimpleFilterable} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see SimpleFilterable
     */
    public interface FluentSimpleFilterable<S extends FluentHierarchical<S>>
            extends SimpleFilterable, FluentContainer<S> {

        // Javadoc copied form Vaadin Framework
        /**
         * Add a filter for given property.
         * <p>
         * The API {@link Filterable#addContainerFilter(Filter)} is recommended
         * instead of this method. A {@link SimpleStringFilter} can be used with
         * the new API to implement the old string filtering functionality.
         * <p>
         * The filter accepts items for which toString() of the value of the
         * given property contains or starts with given filterString. Other
         * items are not visible in the container when filtered.
         * <p>
         * If a container has multiple filters, only items accepted by all
         * filters are visible.
         *
         * @param propertyId Property for which the filter is applied to.
         * @param filterString String that must match the value of the property
         * @param ignoreCase Determine if the casing can be ignored when
         * comparing strings.
         * @param onlyMatchPrefix Only match prefixes; no other matches are
         * @return this (for method chaining) included.
         * @see #addContainerFilter(java.lang.Object, java.lang.String, boolean,
         * boolean)
         */
        public S withContainerFilter(Object propertyId, String filterString,
                boolean ignoreCase, boolean onlyMatchPrefix);

    }

    /**
     * A {@link Filterable} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Filterable
     */
    public interface FluentFilterable<S extends FluentFilterable<S>>
            extends Filterable, FluentContainer<S> {

        // Javadoc copied form Vaadin Framework
        /**
         * Adds a filter for the container.
         * <p>
         * If a container has multiple filters, only items accepted by all
         * filters are visible.
         * 
         * @param filter The filter to add
         * @return this (for method chaining)
         * @throws UnsupportedFilterException if the filter is not supported by
         * the container
         */
        public S withContainerFilter(Filter filter)
                throws UnsupportedFilterException;
        
    }

    /**
     * A {@link Viewer} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Viewer
     */
    public interface FluentViewer<S extends FluentViewer<S>> extends Viewer {

        // Javadoc copied form Vaadin Framework
        /**
         * Sets the Container that serves as the data source of the viewer.
         *
         * @param newDataSource The new data source Item
         * @return this (for method chaining)
         * @see #setContainerDataSource(com.vaadin.data.Container)
         */
        public S withContainerDataSource(Container newDataSource);

    }

    // Javadoc copied form Vaadin Framework
    /**
     * An {@link Editor} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see Editor
     */
    public interface FluentEditor<S extends FluentEditor<S>> extends Editor, FluentViewer<S> {

    }

    /**
     * An {@link ItemSetChangeNotifier} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see ItemSetChangeNotifier
     */
    public interface FluentItemSetChangeNotifier<S extends FluentItemSetChangeNotifier<S>>
            extends ItemSetChangeNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Adds an Item set change listener for the object.
         *
         * @param listener listener to be added
         * @return this (for method chaining)
         * @see
         * #addItemSetChangeListener(com.vaadin.data.Container.ItemSetChangeListener)
         */
        public S withItemSetChangeListener(
                Container.ItemSetChangeListener listener);

    }

    /**
     * A {@link PropertySetChangeNotifier} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see PropertySetChangeNotifier
     */
    public interface FluentPropertySetChangeNotifier<S extends FluentPropertySetChangeNotifier<S>>
            extends PropertySetChangeNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Registers a new Property set change listener for this Container.
         *
         * @param listener The new Listener to be registered
         * @return this (for method chaining)
         * @see
         * #addPropertySetChangeListener(com.vaadin.data.Container.PropertySetChangeListener)
         */
        public S withPropertySetChangeListener(
                Container.PropertySetChangeListener listener);

    }

}
