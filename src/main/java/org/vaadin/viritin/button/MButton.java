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

import java.util.LinkedHashSet;

import com.vaadin.server.Resource;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.Button;
import org.vaadin.viritin.MSize;

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

    public MButton(Resource icon, String caption, ClickListener listener) {
        super(null, listener);
        setIcon(icon);
        setCaption(caption);
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

    public MButton withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MButton withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    public MButton withDescription(String description) {
        setDescription(description);
        return this;
    }

    public MButton withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    public MButton withId(String id) {
        setId(id);
        return this;
    }

    public MButton withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MButton withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MButton withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MButton withHeight(String height) {
        setHeight(height);
        return this;
    }

    public MButton withHeight(float height, Unit unit) {
        setHeight(height, unit);
        return this;
    }

    public MButton withFullHeight() {
        setHeight("100%");
        return this;
    }

    public MButton withSize(MSize size) {
        setWidth(size.getWidth(), size.getWidthUnit());
        setHeight(size.getHeight(), size.getHeightUnit());
        return this;
    }

    public MButton withClickShortcut(int keycode, int... modifiers){
        setClickShortcut(keycode, modifiers);
        return this;
    }

    /**
     * A parameterless version of ClickListener to make it easier to use method
     * references.
     */
    public interface MClickListener {

        void onClick();
    }

    @Override
    protected void fireClick(MouseEventDetails details) {
        super.fireClick(details);
        if (mClickListeners != null) {
            final MClickListener[] array = mClickListeners.toArray(
                    new MClickListener[mClickListeners.size()]);
            for (MClickListener l : array) {
                l.onClick();
            }
        }
    }

    private LinkedHashSet<MClickListener> mClickListeners;

    public MButton addClickListener(MClickListener listener) {
        if (mClickListeners == null) {
            mClickListeners = new LinkedHashSet<>();
        }
        mClickListeners.add(listener);
        return this;
    }

    public MButton removeClickListener(MClickListener listener) {
        if (mClickListeners != null) {
            mClickListeners.remove(listener);
        }
        return this;
    }

}
