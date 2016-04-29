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

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * A {@link ComponentContainer} complemented by fluent setters.
 * 
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see ComponentContainer
 */
public interface FluentComponentContainer<S extends FluentComponentContainer<S>>
        extends ComponentContainer, FluentHasComponents<S>,
        FluentHasComponents.FluentComponentAttachDetachNotifier<S> {

    // Javadoc copied form Vaadin Framework
    /**
     * Adds the component into this container.
     *
     * @param c the component to be added.
     * @return this (for method chaining)
     * @see #addComponent(com.vaadin.ui.Component)
     */
    public S withComponent(Component c);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds the components in the given order to this component container.
     *
     * @param components The components to add.
     * @return this (for method chaining)
     * @see #addComponents(com.vaadin.ui.Component...)
     */
    public S withComponents(Component... components);

}
