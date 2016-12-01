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
package org.vaadin.viritin.button;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;

/**
 *
 * @author mattitahvonenitmill
 */
public class ConfirmButton extends MButton {

    private static final long serialVersionUID = -3146998691761708825L;

    private String confirmWindowCaption;
    private String confirmationText = "Are you sure?";
    private String okCaption = "OK";
    private String cancelCaption = "Cancel";

    private String confirmWindowOkButtonStyle;

    public ConfirmButton() {
    }

    public ConfirmButton(String buttonCaption, String confirmationText,
            ClickListener listener) {
        super(buttonCaption, listener);
        this.confirmationText = confirmationText;
    }

    public ConfirmButton(Resource icon, String confirmationText,
            ClickListener listener) {
        super(icon, listener);
        this.confirmationText = confirmationText;
    }

    public ConfirmButton(Resource icon, String buttonCaption, String confirmationText,
            ClickListener listener) {
        super(icon, buttonCaption, listener);
        this.confirmationText = confirmationText;
    }

    @Override
    protected void fireClick(final MouseEventDetails details) {
        ConfirmDialog dialog = ConfirmDialog.show(getUI(), getConfirmWindowCaption(),
                getConfirmationText(), getOkCaption(), getCancelCaption(), new Runnable() {
            @Override
            public void run() {
                doFireClickListener(details);
            }
        });

        dialog.getOkButton().addStyleName(confirmWindowOkButtonStyle);
    }

    @Override
    protected void fireClick() {
        fireClick(null);
    }

    @Override
    public void setClickShortcut(int keyCode, int... modifiers) {
        super.setClickShortcut(keyCode, modifiers); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public ConfirmButton removeClickListener(MClickListener listener) {
        super.removeClickListener(listener);

        return this;
    }

    @Override
    public ConfirmButton addClickListener(MClickListener listener) {
        super.addClickListener(listener);

        return this;
    }

    @Override
    public ConfirmButton withFullHeight() {
        super.withFullHeight();

        return this;
    }

    @Override
    public ConfirmButton withHeight(float height, Unit unit) {
        super.withHeight(height, unit);

        return this;
    }

    @Override
    public ConfirmButton withHeight(String height) {
        super.withHeight(height);

        return this;
    }

    @Override
    public ConfirmButton withFullWidth() {
        super.withFullWidth();

        return this;
    }

    @Override
    public ConfirmButton withWidth(float width, Unit unit) {
        super.withWidth(width, unit);

        return this;
    }

    @Override
    public ConfirmButton withWidth(String width) {
        super.withWidth(width);

        return this;
    }

    @Override
    public ConfirmButton withId(String id) {
        super.withId(id);

        return this;
    }

    @Override
    public ConfirmButton withVisible(boolean visible) {
        super.withVisible(visible);

        return this;
    }

    @Override
    public ConfirmButton withDescription(String description) {
        super.withDescription(description);

        return this;
    }

    @Override
    public ConfirmButton withCaption(String caption) {
        super.withCaption(caption);

        return this;
    }

    @Override
    public ConfirmButton withStyleName(String... styleNames) {
        super.withStyleName(styleNames);

        return this;
    }

    @Override
    public ConfirmButton withListener(ClickListener listener) {
        super.withListener(listener);

        return this;
    }

    @Override
    public ConfirmButton withIcon(Resource icon) {
        super.withIcon(icon);

        return this;
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

    public ConfirmButton withI18NCaption(String okCaption, String cancelCaption) {
        this.okCaption = okCaption;
        this.cancelCaption = cancelCaption;
        return this;
    }

    @Override
    public ConfirmButton withClickShortcut(int keycode, int... modifiers) {
        setClickShortcut(keycode, modifiers);
        return this;
    }

    public void setConfirmWindowOkButtonStyle(String confirmWindowOkButtonStyle) {
        this.confirmWindowOkButtonStyle = confirmWindowOkButtonStyle;
    }
}
