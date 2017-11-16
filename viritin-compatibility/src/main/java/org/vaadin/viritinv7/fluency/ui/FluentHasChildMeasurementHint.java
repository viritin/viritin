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
package org.vaadin.viritinv7.fluency.ui;

import com.vaadin.ui.HasChildMeasurementHint;

/**
 * A {@link HasChildMeasurementHint} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see HasChildMeasurementHint
 */
public interface FluentHasChildMeasurementHint<S extends FluentHasChildMeasurementHint<S>>
        extends HasChildMeasurementHint {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets desired child size measurement hint.
     *
     * @param hint desired hint. A value of null will reset value back to the
     * default (MEASURE_ALWAYS)
     * @return this (for method chaining)
     * @see
     * #withChildMeasurementHint(com.vaadin.ui.HasChildMeasurementHint.ChildMeasurementHint)
     */
    public S withChildMeasurementHint(ChildMeasurementHint hint);

}
