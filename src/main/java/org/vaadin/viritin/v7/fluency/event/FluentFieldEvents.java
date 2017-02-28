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
package org.vaadin.viritin.v7.fluency.event;

import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.v7.event.FieldEvents;

/**
 * A {@link FieldEvents} complemented by fluent setters.
 *
 * @author Max Schuster
 * @see FieldEvents
 */
public interface FluentFieldEvents extends FieldEvents {

    /**
     * A {@link FocusNotifier} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see FocusNotifier
     */
    public interface FluentFocusNotifier<S extends FluentFocusNotifier<S>>
            extends FocusNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Adds a <code>FocusListener</code> to the Component which gets fired
         * when a <code>Field</code> receives keyboard focus.
         *
         * @param listener The focus listener to add
         * @return this (for method chaining)
         * @see #addFocusListener(com.vaadin.event.FieldEvents.FocusListener)
         */
        public default S withFocusListener(FocusListener listener) {
            ((FocusNotifier) this).addFocusListener(listener);
            return (S) this;
        }

    }

    /**
     * A {@link BlurNotifier} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see BlurNotifier
     */
    public interface FluentBlurNotifier<S extends FluentBlurNotifier<S>>
            extends BlurNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Adds a <code>BlurListener</code> to the Component which gets fired
         * when a <code>Field</code> loses keyboard focus.
         *
         * @param listener The blur listener to add
         * @return this (for method chaining)
         * @see #addBlurListener(com.vaadin.event.FieldEvents.BlurListener)
         */
        public default S withBlurListener(BlurListener listener) {
            ((BlurNotifier) this).addBlurListener(listener);
            return (S) this;
        }

    }

    /**
     * A {@link TextChangeNotifier} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see TextChangeNotifier
     */
    public interface FluentTextChangeNotifier<S extends FluentTextChangeNotifier<S>>
            extends TextChangeNotifier {

        // Javadoc copied form Vaadin Framework
        /**
         * Adds a <code>TextChangeListener</code> to the Component which gets
         * fired when the text of the <code>Field</code> changes.
         *
         * @param listener The text change listener to add
         * @return this (for method chaining)
         * @see
         * #addTextChangeListener(com.vaadin.event.FieldEvents.TextChangeListener)
         */
        public default S withTextChangeListener(TextChangeListener listener) {
            ((TextChangeNotifier) this).addTextChangeListener(listener);
            return (S) this;
        }

    }

}
