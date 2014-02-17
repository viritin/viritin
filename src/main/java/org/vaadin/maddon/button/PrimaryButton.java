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

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;

/**
 * A primary button, commonly used for e.g. saving an entity. Automatically sets 
 * "primary" class name and hooks click shortcut for ENTER.
 */
public class PrimaryButton extends Button {

    public PrimaryButton() {
        setupPrimaryButton();
    }

    public PrimaryButton(String caption) {
        super(caption);
        setupPrimaryButton();
    }

    public PrimaryButton(String caption, ClickListener listener) {
        super(caption, listener);
        setupPrimaryButton();
    }

    private void setupPrimaryButton() {
        setStyleName("primary default");
        setClickShortcut(ShortcutAction.KeyCode.ENTER, null);
    }
    
}
