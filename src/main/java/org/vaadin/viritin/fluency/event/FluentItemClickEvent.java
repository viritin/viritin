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
package org.vaadin.viritin.fluency.event;

import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Component;

/**
 * An {@link ItemClickNotifier} complemented by fluent setters.
 *
 * @author Max Schuster
 * @see ItemClickEvent
 */
public class FluentItemClickEvent extends ItemClickEvent {

    public FluentItemClickEvent(Component source, Item item, Object itemId,
            Object propertyId, MouseEventDetails details) {
        super(source, item, itemId, propertyId, details);
    }

    // Javadoc copied form Vaadin Framework
    /**
     * An {@link ItemClickNotifier} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see ItemClickNotifier
     */
    public interface FluentItemClickNotifier<S extends FluentItemClickNotifier<S>>
            extends ItemClickNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Register a listener to handle {@link ItemClickEvent}s.
         *
         * @param listener ItemClickListener to be registered
         * @return this (for method chaining)
         * @see #addItemClickListener(com.vaadin.event.ItemClickEvent.ItemClickListener)
         */
        public S withItemClickListener(ItemClickListener listener);
    }

}
