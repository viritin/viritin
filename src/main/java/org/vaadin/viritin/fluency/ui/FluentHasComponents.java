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
package org.vaadin.viritin.fluency.ui;

import com.vaadin.ui.HasComponents;

/**
 * A {@link HasComponents} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S>
 *            Self-referential generic type
 * @see HasComponents
 */
public interface FluentHasComponents<S extends FluentHasComponents<S>>
        extends HasComponents, FluentComponent<S> {

    /**
     * A {@link ComponentAttachDetachNotifier} complemented by fluent setters.
     *
     * @param <S>
     *            Self-referential generic type
     * @see ComponentAttachDetachNotifier
     */
    public interface FluentComponentAttachDetachNotifier<S extends FluentHasComponents<S>>
            extends ComponentAttachDetachNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Listens the component attach events.
         *
         * @param listener
         *            the listener to add.
         * @return this (for method chaining)
         * @see #addComponentAttachListener(com.vaadin.ui.HasComponents.ComponentAttachListener)
         */
        public default S withComponentAttachListener(
                ComponentAttachListener listener) {
            ((ComponentAttachDetachNotifier) this)
                    .addComponentAttachListener(listener);
            return (S) this;
        }

        // Javadoc copied form Vaadin Framework
        /**
         * Listens the component detach events.
         *
         * @param listener
         *            the listener to add.
         * @return this (for method chaining)
         * @see #addComponentDetachListener(com.vaadin.ui.HasComponents.ComponentDetachListener)
         */
        public default S withComponentDetachListener(
                ComponentDetachListener listener) {
            ((ComponentAttachDetachNotifier) this)
                    .addComponentDetachListener(listener);
            return (S) this;
        }

    }

}
