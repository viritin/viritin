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

import com.vaadin.v7.data.Validatable;
import com.vaadin.v7.data.Validator;

/**
 * A {@link Validatable} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see Validatable
 */
public interface FluentValidatable<S extends FluentValidatable<S>>
        extends Validatable {

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a new validator for this object. The validator's
     * {@link Validator#validate(Object)} method is activated every time the
     * object's value needs to be verified, that is, when the {@link #isValid()}
     * method is called. This usually happens when the object's value changes.
     *
     * @param validator the new validator
     * @return this (for method chaining)
     * @see #addValidator(com.vaadin.data.Validator)
     */
    public default S withValidator(Validator validator) {
        ((Validatable) this).addValidator(validator);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Should the validabtable object accept invalid values. Supporting this
     * configuration possibility is optional. By default invalid values are
     * allowed.
     *
     * @param invalidValueAllowed Accept invalid values
     * @throws UnsupportedOperationException if the setInvalidAllowed is not
     * supported.
     * @return this (for method chaining)
     * @see #setInvalidAllowed(boolean)
     */
    public default S withInvalidAllowed(boolean invalidValueAllowed)
            throws UnsupportedOperationException {
        ((Validatable) this).setInvalidAllowed(invalidValueAllowed);
        return (S) this;
    }

}
