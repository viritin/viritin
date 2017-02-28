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

import com.vaadin.v7.data.BufferedValidatable;

/**
 * A {@link BufferedValidatable} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see BufferedValidatable
 */
public interface FluentBufferedValidatable<S extends FluentBufferedValidatable<S>>
        extends BufferedValidatable, FluentBuffered<S>,
        FluentValidatable<S> {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets if the invalid data should be committed to datasource. The default
     * is <code>false</code>.
     *
     * @param isCommitted invalid data should be committed to datasource
     * @return this (for method chaining)
     * @see #setInvalidCommitted(boolean)
     */
    public default S withInvalidCommitted(boolean isCommitted) {
        ((BufferedValidatable) this).setInvalidCommitted(isCommitted);
        return (S) this;
    }

}
