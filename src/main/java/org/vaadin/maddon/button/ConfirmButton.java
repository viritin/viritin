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

import com.vaadin.shared.MouseEventDetails;
import org.vaadin.dialogs.ConfirmDialog;

/**
 *
 * @author mattitahvonenitmill
 */
public class ConfirmButton extends MButton {

    private String confirmWindowCaption;
    private String confirmationText = "Are you sure?";
    private String okCaption = "OK";
    private String cancelCaption = "Cancel";

    public ConfirmButton() {
    }

    public ConfirmButton(String buttonCaption, String confirmationText, ClickListener listener) {
        super(buttonCaption, listener);
        this.confirmationText = confirmationText;
    }

    @Override
    protected void fireClick(final MouseEventDetails details) {
        ConfirmDialog.show(getUI(), getConfirmWindowCaption(),
                getConfirmationText(), getOkCaption(), getCancelCaption(),
                new Runnable() {

                    @Override
                    public void run() {
                        doFireClickListener(details);
                    }

                });

    }

    protected void doFireClickListener(final MouseEventDetails details) {
        ConfirmButton.super.fireClick(details);
    }

    public String getConfirmWindowCaption() {
        return confirmWindowCaption;
    }

    public ConfirmButton setConfirmWindowCaption(String confirmWindowCaption) {
        this.confirmWindowCaption = confirmWindowCaption;
        return this;
    }

    public String getConfirmationText() {
        return confirmationText;
    }

    public ConfirmButton setConfirmationText(String confirmationText) {
        this.confirmationText = confirmationText;
        return this;
    }

    public String getOkCaption() {
        return okCaption;
    }

    public ConfirmButton setOkCaption(String okCaption) {
        this.okCaption = okCaption;
        return this;
    }

    public String getCancelCaption() {
        return cancelCaption;
    }

    public ConfirmButton setCancelCaption(String cancelCaption) {
        this.cancelCaption = cancelCaption;
        return this;
    }

}
