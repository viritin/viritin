/*
 * Copyright 2016 martenpriess.
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
package org.vaadin.viritin.label;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.vaadin.viritin.MSize;

/**
 * Fluent Label
 */
public class MLabel extends Label {

    public MLabel() {
        super();
    }

    public MLabel(String content) {
        super(content);
    }

    public MLabel(String caption, String content) {
        super(content);
        setCaption(caption);
    }

    public MLabel withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    /**
     * equals to withValue
     *
     * @param content value of label
     * @return itself
     */
    public MLabel withContent(String content) {
        setValue(content);
        return this;
    }

    public MLabel withValue(String value) {
        setValue(value);
        return this;
    }

    public MLabel withContentMode(ContentMode mode) {
        setContentMode(mode);
        return this;
    }

    public MLabel withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MLabel withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MLabel withSize(String width, String height) {
        setWidth(width);
        setHeight(height);
        return this;
    }

    public MLabel withSize(MSize mSize) {
        setWidth(mSize.getWidth(), mSize.getWidthUnit());
        setHeight(mSize.getHeight(), mSize.getHeightUnit());
        return this;
    }

    public MLabel withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MLabel withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MLabel withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MLabel withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    public MLabel withEnabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    public MLabel withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public MLabel withId(String id) {
        setId(id);
        return this;
    }

}
