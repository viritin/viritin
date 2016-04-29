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

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.fluency.server.FluentSizeable;

/**
 * A {@link Component} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see Component
 */
public interface FluentComponent<S extends FluentComponent<S>> extends 
        Component, FluentSizeable<S> {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the caption of the component.
     *
     * <p>
     * A <i>caption</i> is an explanatory textual label accompanying a user
     * interface component, usually shown above, left of, or inside the
     * component. <i>Icon</i> (see {@link #setIcon(Resource) setIcon()} is
     * closely related to caption and is usually displayed horizontally before
     * or after it, depending on the component and the containing layout.
     * </p>
     *
     * <p>
     * The caption can usually also be given as the first parameter to a
     * constructor, though some components do not support it.
     * </p>
     *
     * <pre>
     * RichTextArea area = new RichTextArea();
     * area.setCaption(&quot;You can edit stuff here&quot;);
     * area.setValue(&quot;&lt;h1&gt;Helpful Heading&lt;/h1&gt;&quot;
     *         + &quot;&lt;p&gt;All this is for you to edit.&lt;/p&gt;&quot;);
     * </pre>
     *
     * <p>
     * The contents of a caption are automatically quoted, so no raw HTML can be
     * rendered in a caption. The validity of the used character encoding,
     * usually UTF-8, is not checked.
     * </p>
     *
     * <p>
     * The caption of a component is, by default, managed and displayed by the
     * layout component or component container in which the component is placed.
     * For example, the {@link VerticalLayout} component shows the captions
     * left-aligned above the contained components, while the {@link FormLayout}
     * component shows the captions on the left side of the vertically laid
     * components, with the captions and their associated components
     * left-aligned in their own columns. The {@link CustomComponent} does not
     * manage the caption of its composition root, so if the root component has
     * a caption, it will not be rendered. Some components, such as
     * {@link Button} and {@link Panel}, manage the caption themselves and
     * display it inside the component.
     * </p>
     *
     * <p>
     * A reimplementation should call the superclass implementation.
     * </p>
     *
     * @param caption the new caption for the component. If the caption is
     * {@code null}, no caption is shown and it does not normally take any space
     * @return this (for method chaining)
     * @see #setCaption(java.lang.String)
     */
    public S withCaption(String caption);

    // Javadoc copied form Vaadin Framework
    /**
     * Enables or disables the component. The user can not interact with
     * disabled components, which are shown with a style that indicates the
     * status, usually shaded in light gray color. Components are enabled by
     * default.
     *
     * <pre>
     * Button enabled = new Button(&quot;Enabled&quot;);
     * enabled.setEnabled(true); // The default
     * layout.addComponent(enabled);
     *
     * Button disabled = new Button(&quot;Disabled&quot;);
     * disabled.setEnabled(false);
     * layout.addComponent(disabled);
     * </pre>
     *
     * @param enabled a boolean value specifying if the component should be
     * enabled or not
     * @return this (for method chaining)
     * @see #setEnabled(boolean)
     */
    public S withEnabled(boolean enabled);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the icon of the component.
     *
     * <p>
     * An icon is an explanatory graphical label accompanying a user interface
     * component, usually shown above, left of, or inside the component. Icon is
     * closely related to caption (see {@link #setCaption(String) setCaption()})
     * and is usually displayed horizontally before or after it, depending on
     * the component and the containing layout.
     * </p>
     *
     * <p>
     * The image is loaded by the browser from a resource, typically a
     * {@link com.vaadin.server.ThemeResource}.
     * </p>
     *
     * <pre>
     * // Component with an icon from a custom theme
     * TextField name = new TextField(&quot;Name&quot;);
     * name.setIcon(new ThemeResource(&quot;icons/user.png&quot;));
     * layout.addComponent(name);
     *
     * // Component with an icon from another theme ('runo')
     * Button ok = new Button(&quot;OK&quot;);
     * ok.setIcon(new ThemeResource(&quot;../runo/icons/16/ok.png&quot;));
     * layout.addComponent(ok);
     * </pre>
     *
     * <p>
     * The icon of a component is, by default, managed and displayed by the
     * layout component or component container in which the component is placed.
     * For example, the {@link VerticalLayout} component shows the icons
     * left-aligned above the contained components, while the {@link FormLayout}
     * component shows the icons on the left side of the vertically laid
     * components, with the icons and their associated components left-aligned
     * in their own columns. The {@link CustomComponent} does not manage the
     * icon of its composition root, so if the root component has an icon, it
     * will not be rendered.
     * </p>
     *
     * <p>
     * An icon will be rendered inside an HTML element that has the
     * {@code v-icon} CSS style class. The containing layout may enclose an icon
     * and a caption inside elements related to the caption, such as
     * {@code v-caption} .
     * </p>
     *
     * @param icon the icon of the component. If null, no icon is shown and it
     * does not normally take any space.
     * @return this (for method chaining)
     * @see #setIcon(com.vaadin.server.Resource)
     * @see #setCaption(String)
     */
    public S withIcon(Resource icon);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds an unique id for component that is used in the client-side for
     * testing purposes. Keeping identifiers unique is the responsibility of the
     * programmer.
     *
     * @param id An alphanumeric id
     * @return this (for method chaining)
     * @see #setId(java.lang.String)
     */
    public S withId(String id);

    // Javadoc copied form Vaadin Framework
    /**
     * Changes the primary style name of the component.
     *
     * <p>
     * The primary style name identifies the component when applying the CSS
     * theme to the Component. By changing the style name all CSS rules targeted
     * for that style name will no longer apply, and might result in the
     * component not working as intended.
     * </p>
     *
     * <p>
     * To preserve the original style of the component when changing to a new
     * primary style you should make your new primary style inherit the old
     * primary style using the SASS @include directive. See more in the SASS
     * tutorials.
     * </p>
     *
     * @param style The new primary style name
     * @return this (for method chaining)
     * @see #setPrimaryStyleName(java.lang.String)
     */
    public S withPrimaryStyleName(String style);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the read-only mode of the component to the specified mode. The user
     * can not change the value of a read-only component.
     *
     * <p>
     * As only {@link Field} components normally have a value that can be input
     * or changed by the user, this is mostly relevant only to field components,
     * though not restricted to them.
     * </p>
     *
     * <p>
     * Notice that the read-only mode only affects whether the user can change
     * the <i>value</i> of the component; it is possible to, for example, scroll
     * a read-only table.
     * </p>
     *
     * @param readOnly a boolean value specifying whether the component is put
     * read-only mode or not
     * @return this (for method chaining)
     * @see #setReadOnly(boolean)
     */
    public S withReadOnly(boolean readOnly);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets one or more user-defined style names of the component, replacing any
     * previous user-defined styles. Multiple styles can be specified as a
     * space-separated list of style names. The style names must be valid CSS
     * class names and should not conflict with any built-in style names in
     * Vaadin or GWT.
     *
     * <pre>
     * Label label = new Label(&quot;This text has a lot of style&quot;);
     * label.setStyleName(&quot;myonestyle myotherstyle&quot;);
     * </pre>
     *
     * <p>
     * Each style name will occur in two versions: one as specified and one that
     * is prefixed with the style name of the component. For example, if you
     * have a {@code Button} component and give it "{@code mystyle}" style, the
     * component will have both "{@code mystyle}" and "{@code v-button-mystyle}"
     * styles. You could then style the component either with:
     * </p>
     *
     * <pre>
     * .myonestyle {background: blue;}
     * </pre>
     *
     * <p>
     * or
     * </p>
     *
     * <pre>
     * .v-button-myonestyle {background: blue;}
     * </pre>
     *
     * <p>
     * It is normally a good practice to use {@link #addStyleName(String)
     * addStyleName()} rather than this setter, as different software
     * abstraction layers can then add their own styles without accidentally
     * removing those defined in other layers.
     * </p>
     *
     * @param styles the new style or styles of the component as a
     * space-separated list
     * @return this (for method chaining)
     * @see #getStyleName()
     * @see #addStyleName(String)
     * @see #removeStyleName(String)
     * @see #setStyleName(java.lang.String)
     */
    public S withStyleName(String... styles);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the visibility of the component.
     *
     * <p>
     * Visible components are drawn in the user interface, while invisible ones
     * are not. The effect is not merely a cosmetic CSS change - no information
     * about an invisible component will be sent to the client. The effect is
     * thus the same as removing the component from its parent.
     * </p>
     *
     * <pre>
     * TextField readonly = new TextField(&quot;Read-Only&quot;);
     * readonly.setValue(&quot;You can't see this!&quot;);
     * readonly.setVisible(false);
     * layout.addComponent(readonly);
     * </pre>
     *
     * <p>
     * A component is visible only if all of its parents are also visible. If a
     * component is explicitly set to be invisible, changes in the visibility of
     * its parents will not change the visibility of the component.
     * </p>
     *
     * @param visible the boolean value specifying if the component should be
     * visible after the call or not.
     * @return this (for method chaining)
     * @see #setVisible(boolean)
     * @see #isVisible()
     */
    public S withVisible(boolean visible);

    // Javadoc copied form Vaadin Framework
    /**
     * Registers a new (generic) component event listener for the component.
     *
     * <pre>
     * class Listening extends CustomComponent implements Listener {
     *     // Stored for determining the source of an event
     *     Button ok;
     *
     *     Label status; // For displaying info about the event
     *
     *     public Listening() {
     *         VerticalLayout layout = new VerticalLayout();
     *
     *         // Some miscellaneous component
     *         TextField name = new TextField(&quot;Say it all here&quot;);
     *         name.addListener(this);
     *         name.setImmediate(true);
     *         layout.addComponent(name);
     *
     *         // Handle button clicks as generic events instead
     *         // of Button.ClickEvent events
     *         ok = new Button(&quot;OK&quot;);
     *         ok.addListener(this);
     *         layout.addComponent(ok);
     *
     *         // For displaying information about an event
     *         status = new Label(&quot;&quot;);
     *         layout.addComponent(status);
     *
     *         setCompositionRoot(layout);
     *     }
     *
     *     public void componentEvent(Event event) {
     *         // Act according to the source of the event
     *         if (event.getSource() == ok)
     *             getWindow().showNotification(&quot;Click!&quot;);
     *
     *         status.setValue(&quot;Event from &quot; + event.getSource().getClass().getName()
     *                 + &quot;: &quot; + event.getClass().getName());
     *     }
     * }
     *
     * Listening listening = new Listening();
     * layout.addComponent(listening);
     * </pre>
     *
     * @param listener the new Listener to be registered.
     * @return this (for method chaining)
     * @see Component.Event
     * @see #addListener(com.vaadin.ui.Component.Listener)
     */
    public S withListener(Component.Listener listener);

    /**
     * A {@link Focusable} complemented by fluent setters.
     * 
     * @param <C> Fluent component type
     * @see Focusable
     */
    public interface FluentFocusable<C extends FluentFocusable<C>> extends 
            Focusable, FluentComponent<C> {

        // Javadoc copied form Vaadin Framework
        /**
         * Sets the <i>tabulator index</i> of the {@code Focusable} component.
         * The tab index property is used to specify the order in which the
         * fields are focused when the user presses the Tab key. Components with
         * a defined tab index are focused sequentially first, and then the
         * components with no tab index.
         *
         * <pre>
         * Form loginBox = new Form();
         * loginBox.setCaption(&quot;Login&quot;);
         * layout.addComponent(loginBox);
         *
         * // Create the first field which will be focused
         * TextField username = new TextField(&quot;User name&quot;);
         * loginBox.addField(&quot;username&quot;, username);
         *
         * // Set focus to the user name
         * username.focus();
         *
         * TextField password = new TextField(&quot;Password&quot;);
         * loginBox.addField(&quot;password&quot;, password);
         *
         * Button login = new Button(&quot;Login&quot;);
         * loginBox.getFooter().addComponent(login);
         *
         * // An additional component which natural focus order would
         * // be after the button.
         * CheckBox remember = new CheckBox(&quot;Remember me&quot;);
         * loginBox.getFooter().addComponent(remember);
         *
         * username.setTabIndex(1);
         * password.setTabIndex(2);
         * remember.setTabIndex(3); // Different than natural place
         * login.setTabIndex(4);
         * </pre>
         *
         * <p>
         * After all focusable user interface components are done, the browser
         * can begin again from the component with the smallest tab index, or it
         * can take the focus out of the page, for example, to the location bar.
         * </p>
         *
         * <p>
         * If the tab index is not set (is set to zero), the default tab order
         * is used. The order is somewhat browser-dependent, but generally
         * follows the HTML structure of the page.
         * </p>
         *
         * <p>
         * A negative value means that the component is completely removed from
         * the tabulation order and can not be reached by pressing the Tab key
         * at all.
         * </p>
         *
         * @param tabIndex the tab order of this component. Indexes usually
         * start from 1. Zero means that default tab order should be used. A
         * negative value means that the field should not be included in the
         * tabbing sequence.
         * @return this (for method chaining)
         * @see #setTabIndex(int)
         */
        public C withTabIndex(int tabIndex);

    }

}
