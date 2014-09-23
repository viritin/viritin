/*
 * Copyright 2014 Matti Tahvonen.
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
package org.vaadin.maddon.button;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;

/**
 * An extension to basic Vaadin button that adds some handy constructors and
 * fluent API.
 */
public class MButton extends Button {

    public MButton() {
    }

    public MButton(Resource icon) {
        setIcon(icon);
    }

    public MButton(Resource icon, ClickListener listener) {
        super(null, listener);
        setIcon(icon);
    }

    public MButton(String caption) {
        super(caption);
    }

    public MButton(String caption, ClickListener listener) {
        super(caption, listener);
    }

    public MButton withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MButton withListener(ClickListener listener) {
        addClickListener(listener);
        return this;
    }

    public MButton withStyleName(String styleName) {
        setStyleName(styleName);
        return this;
    }

    public MButton withCaption(String caption) {
        setCaption(caption);
        return this;
    }

}
