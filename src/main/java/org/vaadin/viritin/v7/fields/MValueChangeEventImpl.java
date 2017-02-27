/*
 * Copyright 2014 mattitahvonenitmill.
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
package org.vaadin.viritin.v7.fields;

import java.lang.reflect.Method;

import com.vaadin.v7.ui.AbstractField;
import com.vaadin.v7.ui.Field;

public class MValueChangeEventImpl<T> extends AbstractField.ValueChangeEvent implements MValueChangeEvent<T> {

    private static final long serialVersionUID = 7702133432642079889L;

    static final Method VALUE_CHANGE_METHOD;

    static {
        try {
            VALUE_CHANGE_METHOD = MValueChangeListener.class
                    .getDeclaredMethod("valueChange",
                            new Class[]{MValueChangeEvent.class});
        } catch (final java.lang.NoSuchMethodException e) {
            // This should never happen
            throw new java.lang.RuntimeException(
                    "Internal error finding methods in MValueChangeEventImpl");
        }
    }

    public MValueChangeEventImpl(Field source) {
        super(source);
    }

    @Override
    public T getValue() {
        return (T) getProperty().getValue();
    }

}
