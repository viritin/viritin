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
package org.vaadin.viritin.fluency.ui;

import java.util.Locale;

import org.vaadin.viritin.fluency.event.FluentContextClickNotifier;
import org.vaadin.viritin.fluency.server.FluentClientConnector;

import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractComponent;

/**
 * A {@link AbstractComponent} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S>
 *            Self-referential generic type
 * @see AbstractComponent
 */
public interface FluentAbstractComponent<S extends FluentAbstractComponent<S>>
        extends FluentComponent<S>, FluentClientConnector<S>,
        FluentContextClickNotifier<S> {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets whether the caption is rendered as HTML.
     * <p>
     * If set to true, the captions are rendered in the browser as HTML and the
     * developer is responsible for ensuring no harmful HTML is used. If set to
     * false, the caption is rendered in the browser as plain text.
     * <p>
     * The default is false, i.e. to render that caption as plain text.
     *
     * @param captionAsHtml
     *            true if the captions are rendered as HTML, false if rendered
     *            as plain text
     * @return this (for method chaining)
     * @see AbstractComponent#setCaptionAsHtml(boolean)
     */
    public default S withCaptionAsHtml(boolean captionAsHtml) {
        ((AbstractComponent) this).setCaptionAsHtml(captionAsHtml);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the locale of this component.
     *
     * <pre>
     * // Component for which the locale is meaningful
     * InlineDateField date = new InlineDateField(&quot;Datum&quot;);
     *
     * // German language specified with ISO 639-1 language
     * // code and ISO 3166-1 alpha-2 country code.
     * date.setLocale(new Locale(&quot;de&quot;, &quot;DE&quot;));
     *
     * date.setResolution(DateField.RESOLUTION_DAY);
     * layout.addComponent(date);
     * </pre>
     *
     * @param locale
     *            the locale to become this component's locale.
     * @return this (for method chaining)
     * @see AbstractComponent#setLocale(java.util.Locale)
     */
    public default S withLocale(Locale locale) {
        ((AbstractComponent) this).setLocale(locale);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the component's description. See {@link #getDescription()} for more
     * information on what the description is.
     *
     * The description is displayed as HTML in tooltips or directly in certain
     * components so care should be taken to avoid creating the possibility for
     * HTML injection and possibly XSS vulnerabilities.
     *
     * @param description
     *            the new description string for the component.
     * @return this (for method chaining)
     * @see AbstractComponent#setDescription(java.lang.String)
     */
    public default S withDescription(String description) {
        ((AbstractComponent) this).setDescription(description);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the component's error message.
     *
     * @param componentError
     *            the new <code>ErrorMessage</code> of the component.
     * @return this (for method chaining)
     * @see AbstractComponent#setComponentError(com.vaadin.server.ErrorMessage)
     */
    public default S withComponentError(ErrorMessage componentError) {
        ((AbstractComponent) this).setComponentError(componentError);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the data object, that can be used for any application specific data.
     * The component does not use or modify this data.
     *
     * @param data
     *            the Application specific data.
     * @return this (for method chaining)
     * @see AbstractComponent#setData(java.lang.Object)
     */
    public default S withData(Object data) {
        ((AbstractComponent) this).setData(data);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Registers a new shortcut listener for the component.
     *
     * @param shortcut
     *            the new Listener to be registered.
     * @return this (for method chaining)
     * @see AbstractComponent#addShortcutListener(com.vaadin.event.ShortcutListener)
     */
    public default S withShortcutListener(ShortcutListener shortcut) {
        ((AbstractComponent) this).addShortcutListener(shortcut);
        return (S) this;
    }

    // Javadoc copied form Vaadin Framework
    /**
     * Toggles responsiveness of this component.
     *
     * @since 7.5.0
     * @param responsive
     *            boolean enables responsiveness, false disables
     * @return this (for method chaining)
     * @see AbstractComponent#setResponsive(boolean)
     */
    public default S withResponsive(boolean responsive) {
        ((AbstractComponent) this).setResponsive(responsive);
        return (S) this;
    }

    public default S withCaption(String caption, boolean captionAsHtml) {
        setCaption(caption);
        return withCaptionAsHtml(captionAsHtml);
    }

}
