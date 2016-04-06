/*
 * Copyright 2016 Matti Tahvonen.
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
package org.vaadin.viritin.ui;

import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;

/**
 * A {@link Notification} with fluent setters.
 *
 * @author Max Schuster
 * @see Notification
 */
public class MNotification extends Notification {

    /**
     * See: {@link Notification#Notification(java.lang.String)}
     *
     * @param caption The message to show
     * @see Notification#Notification(java.lang.String)
     */
    public MNotification(String caption) {
        super(caption);
    }

    /**
     * See:
     * {@link Notification#Notification(java.lang.String, com.vaadin.ui.Notification.Type)}
     *
     * @param caption The message to show
     * @param type The type of message
     * @see Notification#Notification(java.lang.String,
     * com.vaadin.ui.Notification.Type)
     */
    public MNotification(String caption, Type type) {
        super(caption, type);
    }

    /**
     * See:
     * {@link Notification#Notification(java.lang.String, java.lang.String)}
     *
     * @param caption The message caption
     * @param description The message description
     * @see Notification#Notification(java.lang.String, java.lang.String)
     */
    public MNotification(String caption, String description) {
        super(caption, description);
    }

    /**
     * See:
     * {@link Notification#Notification(java.lang.String, java.lang.String, com.vaadin.ui.Notification.Type)}
     *
     * @param caption The message caption
     * @param description The message description
     * @param type The type of message
     * @see Notification#Notification(java.lang.String, java.lang.String,
     * com.vaadin.ui.Notification.Type)
     */
    public MNotification(String caption, String description, Type type) {
        super(caption, description, type);
    }

    /**
     * See:
     * {@link Notification#Notification(java.lang.String, java.lang.String, com.vaadin.ui.Notification.Type, boolean)}
     *
     * @param caption The message caption
     * @param description The message description
     * @param type The type of message
     * @param htmlContentAllowed Whether html in the caption and description
     * should be displayed as html or as plain text
     * @see Notification#Notification(java.lang.String, java.lang.String,
     * com.vaadin.ui.Notification.Type, boolean)
     */
    public MNotification(String caption, String description, Type type, boolean htmlContentAllowed) {
        super(caption, description, type, htmlContentAllowed);
    }

    /**
     * See: {@link #setHtmlContentAllowed(boolean)}
     *
     * @param htmlContentAllowed true if the texts are used as html, false if
     * used as plain text
     * @return this (for method chaining)
     * @see #setHtmlContentAllowed(boolean)
     */
    public MNotification withHtmlContentAllowed(boolean htmlContentAllowed) {
        setHtmlContentAllowed(htmlContentAllowed);
        return this;
    }

    /**
     * See: {@link #setStyleName(java.lang.String)}
     *
     * @param styleName The new style name.
     * @return this (for method chaining)
     * @see #setStyleName(java.lang.String)
     */
    public MNotification withStyleName(String styleName) {
        setStyleName(styleName);
        return this;
    }

    /**
     * See: {@link #setDelayMsec(int)}
     *
     * @param delayMsec the delay in msec, -1 to require the user to click the
     * message
     * @return this (for method chaining)
     * @see #setDelayMsec(int)
     */
    public MNotification withDelayMsec(int delayMsec) {
        setDelayMsec(delayMsec);
        return this;
    }

    /**
     * See: {@link #setIcon(com.vaadin.server.Resource)}
     *
     * @param icon The desired message icon
     * @return this (for method chaining)
     * @see #setIcon(com.vaadin.server.Resource)
     */
    public MNotification withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    /**
     * See: {@link #setPosition(com.vaadin.shared.Position)}
     *
     * @param position The desired notification position
     * @return this (for method chaining)
     * @see #setPosition(com.vaadin.shared.Position)
     */
    public MNotification withPosition(Position position) {
        setPosition(position);
        return this;
    }

    /**
     * See: {@link #setDescription(java.lang.String)}
     *
     * @param description The message description
     * @return this (for method chaining)
     * @see #setDescription(java.lang.String)
     */
    public MNotification withDescription(String description) {
        setDescription(description);
        return this;
    }

    /**
     * See: {@link #setCaption(java.lang.String)}
     *
     * @param caption The message caption
     * @return this (for method chaining)
     * @see #setCaption(java.lang.String)
     */
    public MNotification withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    /**
     * Shows this notification on the given page {@link Page}.
     *
     * @param page The page on which the notification should be shown.
     * @return this (for method chaining)
     * @see #show(com.vaadin.server.Page)
     */
    public MNotification display(Page page) {
        show(page);
        return this;
    }

    /**
     * Shows this notification on the current {@link Page}
     * ({@link Page#getCurrent()}).
     *
     * @return this (for method chaining)
     * @see #show(com.vaadin.server.Page)
     * @see Page#getCurrent()
     */
    public MNotification display() {
        return display(Page.getCurrent());
    }

    /**
     * Creates a new notification with the given caption and shows it on the
     * current page.
     *
     * @param caption The message caption
     * @return The notification (for method chaining)
     * @see #MNotification(java.lang.String)
     * @see #display()
     */
    public static MNotification display(String caption) {
        return new MNotification(caption).display();
    }

    /**
     * Creates a new notification with the given caption and type and shows it
     * on the current page.
     *
     * @param caption The message caption
     * @param type
     * @return The notification (for method chaining)
     */
    public static MNotification display(String caption, Type type) {
        return new MNotification(caption, type).display();
    }

    /**
     * Creates a new notification with the given caption and type and shows it
     * on the current page.
     *
     * @param caption The message caption
     * @param description The message description
     * @param type
     * @return The notification (for method chaining)
     */
    public static MNotification display(String caption, String description, Type type) {
        return new MNotification(caption, description, type).display();
    }

    /**
     * Creates a {@link Type#HUMANIZED_MESSAGE} notification with the given
     * caption and shows it on the current page.
     *
     * @param caption The message caption
     * @return The notification (for method chaining)
     */
    public static MNotification humanized(String caption) {
        return display(caption, Type.HUMANIZED_MESSAGE);
    }

    /**
     * Creates a {@link Type#HUMANIZED_MESSAGE} notification with the given
     * caption and description and shows it on the current page.
     *
     * @param caption The message caption
     * @param description The message description
     * @return The notification (for method chaining)
     */
    public static MNotification humanized(String caption, String description) {
        return display(caption, description, Type.HUMANIZED_MESSAGE);
    }

    /**
     * Creates a {@link Type#WARNING_MESSAGE} notification with the given
     * caption and shows it on the current page.
     *
     * @param caption The message caption
     * @return The notification (for method chaining)
     */
    public static MNotification warning(String caption) {
        return display(caption, Type.WARNING_MESSAGE);
    }

    /**
     * Creates a {@link Type#WARNING_MESSAGE} notification with the given
     * caption and description and shows it on the current page.
     *
     * @param caption The message caption
     * @param description The message description
     * @return The notification (for method chaining)
     */
    public static MNotification warning(String caption, String description) {
        return display(caption, description, Type.WARNING_MESSAGE);
    }

    /**
     * Creates a {@link Type#ERROR_MESSAGE} notification with the given caption
     * and shows it on the current page.
     *
     * @param caption The message caption
     * @return The notification (for method chaining)
     */
    public static MNotification error(String caption) {
        return display(caption, Type.ERROR_MESSAGE);
    }

    /**
     * Creates a {@link Type#ERROR_MESSAGE} notification with the given caption
     * and description and shows it on the current page.
     *
     * @param caption The message caption
     * @param description The message description
     * @return The notification (for method chaining)
     */
    public static MNotification error(String caption, String description) {
        return display(caption, description, Type.ERROR_MESSAGE);
    }

    /**
     * Creates a {@link Type#TRAY_NOTIFICATION} notification with the given
     * caption and shows it on the current page.
     *
     * @param caption The message caption
     * @return The notification (for method chaining)
     */
    public static MNotification tray(String caption) {
        return display(caption, Type.TRAY_NOTIFICATION);
    }

    /**
     * Creates a {@link Type#TRAY_NOTIFICATION} notification with the given
     * caption and description and shows it on the current page.
     *
     * @param caption The message caption
     * @param description The message description
     * @return The notification (for method chaining)
     */
    public static MNotification tray(String caption, String description) {
        return display(caption, description, Type.TRAY_NOTIFICATION);
    }

    /**
     * Creates a {@link Type#ASSISTIVE_NOTIFICATION} notification with the given
     * caption and shows it on the current page.
     *
     * @param caption The message caption
     * @return The notification (for method chaining)
     */
    public static MNotification assistive(String caption) {
        return display(caption, Type.ASSISTIVE_NOTIFICATION);
    }

    /**
     * Creates a {@link Type#ASSISTIVE_NOTIFICATION} notification with the given
     * caption and description and shows it on the current page.
     *
     * @param caption The message caption
     * @param description The message description
     * @return The notification (for method chaining)
     */
    public static MNotification assistive(String caption, String description) {
        return display(caption, description, Type.ASSISTIVE_NOTIFICATION);
    }

}
