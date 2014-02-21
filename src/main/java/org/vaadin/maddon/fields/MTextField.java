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

package org.vaadin.maddon.fields;

import com.vaadin.data.Property;
import com.vaadin.ui.TextField;

/**
 *
 * @author mattitahvonenitmill
 */
public class MTextField extends TextField {

    public MTextField() {
        configureMaddonStuff();
    }

    private void configureMaddonStuff() {
        setNullRepresentation("");
    }

    public MTextField(String caption) {
        super(caption);
        configureMaddonStuff();
    }

    public MTextField(Property dataSource) {
        super(dataSource);
        configureMaddonStuff();
    }

    public MTextField(String caption, Property dataSource) {
        super(caption, dataSource);
        configureMaddonStuff();
    }

    public MTextField(String caption, String value) {
        super(caption, value);
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        super.addValueChangeListener(listener);
        // Remove once 7.2 is out
        setImmediate(true);
    }

    public TextField withInputPrompt(String inputPrompt) {
        setInputPrompt(inputPrompt);
        return this;
    }

    public TextField withWidth(float width, Unit unit) {
        setWidth(width,unit);
        return this;
    }
    
}
