/*
 * Copyright 2017 Matti Tahvonen.
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
package org.vaadin.viritin.fields;

import com.vaadin.ui.TextField;
import org.vaadin.viritin.fluency.ui.FluentTextField;

/**
 *
 * @author mstahv
 */
public class MTextField extends TextField implements FluentTextField<MTextField> {

    public MTextField() {
    }

    public MTextField(String caption) {
        super(caption);
    }

    public MTextField(String caption, String value) {
        super(caption, value);
    }

    public MTextField(ValueChangeListener<String> valueChangeListener) {
        super(valueChangeListener);
    }

    public MTextField(String caption, ValueChangeListener<String> valueChangeListener) {
        super(caption, valueChangeListener);
    }

    public MTextField(String caption, String value, ValueChangeListener<String> valueChangeListener) {
        super(caption, value, valueChangeListener);
    }
    
}
