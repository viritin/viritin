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
package org.vaadin.viritin.fields;

import org.vaadin.viritin.MSize;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.event.FieldEvents;

/**
 *
 * @author mattitahvonenitmill
 */
public class MTextArea extends FTextArea {

    private static final long serialVersionUID = 2513695658302975814L;

    public MTextArea() {
        configureMaddonStuff();
    }

    private void configureMaddonStuff() {
        setNullRepresentation("");
        setWidth("100%");
    }

    public MTextArea(String caption) {
        super(caption);
        configureMaddonStuff();
    }

    public MTextArea(Property dataSource) {
        super(dataSource);
        configureMaddonStuff();
    }

    public MTextArea(String caption, Property dataSource) {
        super(caption, dataSource);
        configureMaddonStuff();
    }

    public MTextArea(String caption, String value) {
        super(caption, value);
    }

    public MTextArea withFullWidth() {
        return withWidth("100%");
    }
    
    public MTextArea withFullHeight() {
        return withHeight("100%");
    }

    public MTextArea withSize(MSize mSize) {
        setWidth(mSize.getWidth(), mSize.getWidthUnit());
        setHeight(mSize.getHeight(), mSize.getHeightUnit());
        return this;
    }

    public MTextArea withValidator(Validator validator) {
        setImmediate(true);
        addValidator(validator);
        return this;
    }

    public MTextArea withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }
}
