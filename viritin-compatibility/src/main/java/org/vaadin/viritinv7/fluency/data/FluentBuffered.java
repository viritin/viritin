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
package org.vaadin.viritinv7.fluency.data;

import com.vaadin.v7.data.Buffered;

/**
 * A {@link Buffered} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see Buffered
 */
public interface FluentBuffered<S extends FluentBuffered<S>>
        extends Buffered {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the buffered mode to the specified status.
     * <p>
     * When in buffered mode, an internal buffer will be used to store changes
     * until {@link #commit()} is called. Calling {@link #discard()} will revert
     * the internal buffer to the value of the data source.
     * <p>
     * When in non-buffered mode both read and write operations will be done
     * directly on the data source. In this mode the {@link #commit()} and
     * {@link #discard()} methods serve no purpose.
     *
     * @param buffered true if buffered mode should be turned on, false
     * otherwise
     * @return this (for method chaining)
     * @see #setBuffered(boolean) 
     */
    public default S withBuffered(boolean buffered) {
        ((Buffered) this).setBuffered(buffered);
        return (S) this;
    }

}
