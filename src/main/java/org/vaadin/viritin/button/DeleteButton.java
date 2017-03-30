/*
 * Copyright 2015
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

package org.vaadin.viritin.button;

import com.vaadin.server.Resource;
import org.vaadin.viritin.button.MButton.MClickListener;

/**
 * A delete button, commonly used for deleting an entity. Automatically sets
 * "danger" class name and open a confirmation dialog.
 */
public class DeleteButton extends ConfirmButton {

    private static final long serialVersionUID = 4012911261590461969L;

    public DeleteButton() {
        setupDeleteButton();
    }

    public DeleteButton(String caption) {
        setCaption(caption);
        setupDeleteButton();
    }

    public DeleteButton(String caption, String confirmationText, ClickListener listener) {
        super(caption, confirmationText, listener);
        setupDeleteButton();
    }

    public DeleteButton(Resource icon, String caption, String confirmationText, MClickListener listener) {
        super(icon, caption, confirmationText, listener);
        setupDeleteButton();
    }

    private void setupDeleteButton() {
        setStyleName("danger default");
		setConfirmWindowOkButtonStyle("danger default");
    }
}
