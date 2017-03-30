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
import com.vaadin.ui.Button;
import java.util.LinkedHashSet;
import org.vaadin.viritin.button.MButton.MClickListener;
import org.vaadin.viritin.fluency.ui.FluentAbstractComponent;

/**
 *
 * @author mattitahvonenitmill
 */
public class ConfirmButton extends Button implements FluentAbstractComponent<ConfirmButton> {

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
            MClickListener listener) {
        super(icon);
        addClickListener(listener);
        this.confirmationText = confirmationText;
    }

    public ConfirmButton(Resource icon, String buttonCaption, String confirmationText,
            MClickListener listener) {
        super(buttonCaption, icon);
        addClickListener(listener);
        this.confirmationText = confirmationText;
    }
    
    private LinkedHashSet<MClickListener> mClickListeners;

    public ConfirmButton addClickListener(MClickListener listener) {
        if (mClickListeners == null) {
            mClickListeners = new LinkedHashSet<>();
        }
        mClickListeners.add(listener);
        return this;
    }

    public ConfirmButton removeClickListener(MClickListener listener) {
        if (mClickListeners != null) {
            mClickListeners.remove(listener);
        }
        return this;
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
    
    protected void doFireClickListener(final MouseEventDetails details) {
        ConfirmButton.super.fireClick(details);
        if (mClickListeners != null) {
            final MButton.MClickListener[] array = mClickListeners.toArray(
                    new MButton.MClickListener[mClickListeners.size()]);
            for (MButton.MClickListener l : array) {
                l.onClick();
            }
        }
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

    public void setConfirmWindowOkButtonStyle(String confirmWindowOkButtonStyle) {
        this.confirmWindowOkButtonStyle = confirmWindowOkButtonStyle;
    }
}
