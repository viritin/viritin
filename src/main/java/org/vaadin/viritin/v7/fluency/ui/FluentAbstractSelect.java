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
package org.vaadin.viritin.v7.fluency.ui;

import org.vaadin.viritin.v7.fluency.data.FluentContainer;

import com.vaadin.server.Resource;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.v7.ui.AbstractSelect.ItemCaptionMode;

/**
 * An {@link AbstractSelect} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see AbstractSelect
 */
public interface FluentAbstractSelect<S extends FluentAbstractSelect<S>>
        extends FluentAbstractField<S, Object>, FluentContainer<S>,
        FluentContainer.FluentViewer<S>,
        FluentContainer.FluentPropertySetChangeNotifier<S>,
        FluentContainer.FluentItemSetChangeNotifier<S> {

    /**
     * A {@link AbstractSelect.Filtering} complemented by fluent setters.
     *
     * @param <S> Self-referential generic type
     * @see AbstractSelect
     */
    public interface FluentFiltering<S extends FluentFiltering<S>>
            extends AbstractSelect.Filtering {

        // Javadoc copied form Vaadin Framework
        /**
         * Sets the option filtering mode.
         *
         * @param filteringMode the filtering mode to use
         * @see
         * AbstractSelect.Filtering#setFilteringMode(com.vaadin.shared.ui.combobox.FilteringMode)
         */
        public void withFilteringMode(FilteringMode filteringMode);

    }

    // Javadoc copied form Vaadin Framework
    /**
     * TODO refine doc Setter for new item handler that is called when user adds
     * new item in newItemAllowed mode.
     *
     * @param newItemHandler the new item handler
     * @return this (for method chaining)
     * @see
     * AbstractSelect#setNewItemHandler(com.vaadin.ui.AbstractSelect.NewItemHandler)
     */
    public S withNewItemHandler(AbstractSelect.NewItemHandler newItemHandler);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the multiselect mode. Setting multiselect mode false may lose
     * selection information: if selected items set contains one or more
     * selected items, only one of the selected items is kept as selected.
     *
     * Subclasses of AbstractSelect can choose not to support changing the
     * multiselect mode, and may throw {@link UnsupportedOperationException}.
     *
     * @param multiSelect the New value of property multiSelect.
     * @return this (for method chaining)
     * @see AbstractSelect#setMultiSelect(boolean)
     */
    public S withMultiSelect(boolean multiSelect);

    // Javadoc copied form Vaadin Framework
    /**
     * Enables or disables possibility to add new options by the user.
     *
     * @param allowNewOptions the New value of property allowNewOptions.
     * @return this (for method chaining)
     * @see AbstractSelect#setNewItemsAllowed(boolean)
     */
    public S withNewItemsAllowed(boolean allowNewOptions);

    // Javadoc copied form Vaadin Framework
    /**
     * Override the caption of an item. Setting caption explicitly overrides id,
     * item and index captions.
     *
     * @param itemId the id of the item to be recaptioned.
     * @param caption the New caption.
     * @return this (for method chaining)
     * @see AbstractSelect#setItemCaption(java.lang.Object, java.lang.String)
     */
    public S withItemCaption(Object itemId, String caption);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the icon for an item.
     *
     * @param itemId the id of the item to be assigned an icon.
     * @param icon the icon to use or null.
     * @return this (for method chaining)
     * @see AbstractSelect#setItemIcon(java.lang.Object,
     * com.vaadin.server.Resource)
     */
    public S withItemIcon(Object itemId, Resource icon);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the item caption mode.
     *
     * See {@link ItemCaptionMode} for a description of the modes.
     * <p>
     * {@link ItemCaptionMode#EXPLICIT_DEFAULTS_ID} is the default mode.
     * </p>
     *
     * @param mode the One of the modes listed above.
     * @return this (for method chaining)
     * @see AbstractSelect#setItemCaption(java.lang.Object, java.lang.String)
     */
    public S withItemCaptionMode(AbstractSelect.ItemCaptionMode mode);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the item caption property.
     *
     * <p>
     * Setting the id to a existing property implicitly sets the item caption
     * mode to <code>ITEM_CAPTION_MODE_PROPERTY</code>. If the object is in
     * <code>ITEM_CAPTION_MODE_PROPERTY</code> mode, setting caption property id
     * null resets the item caption mode to
     * <code>ITEM_CAPTION_EXPLICIT_DEFAULTS_ID</code>.
     * </p>
     * <p>
     * Note that the type of the property used for caption must be String
     * </p>
     * <p>
     * Setting the property id to null disables this feature. The id is null by
     * default
     * </p>
     * .
     *
     * @param propertyId the id of the property.
     * @return this (for method chaining)
     * @see AbstractSelect#setItemCaptionPropertyId(java.lang.Object)
     */
    public S withItemCaptionPropertyId(Object propertyId);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the item icon property.
     *
     * <p>
     * If the property id is set to a valid value, each item is given an icon
     * got from the given property of the items. The type of the property must
     * be assignable to Resource.
     * </p>
     *
     * <p>
     * Note : The icons set with <code>setItemIcon</code> function override the
     * icons from the property.
     * </p>
     *
     * <p>
     * Setting the property id to null disables this feature. The id is null by
     * default
     * </p>
     * .
     *
     * @param propertyId the id of the property that specifies icons for items
     * or null
     * @return this (for method chaining)
     * @see AbstractSelect#setItemCaptionPropertyId(java.lang.Object)
     * @throws IllegalArgumentException If the propertyId is not in the
     * container or is not of a valid type
     */
    public S withItemIconPropertyId(Object propertyId)
            throws IllegalArgumentException;

    // Javadoc copied form Vaadin Framework
    /**
     * Selects an item.
     *
     * <p>
     * In single select mode selecting item identified by
     * {@link AbstractSelect#getNullSelectionItemId()} sets the value of the
     * property to null.
     * </p>
     *
     * @param itemId the identifier of Item to be selected.
     * @return this (for method chaining)
     * @see AbstractSelect#select(java.lang.Object)
     * @see AbstractSelect#getNullSelectionItemId()
     * @see AbstractSelect#setNullSelectionItemId(Object)
     */
    public S withSelect(Object itemId);

    // Javadoc copied form Vaadin Framework
    /**
     * Allow or disallow empty selection by the user. If the select is in
     * single-select mode, you can make an item represent the empty selection by
     * calling <code>setNullSelectionItemId()</code>. This way you can for
     * instance set an icon and caption for the null selection item.
     *
     * @param nullSelectionAllowed whether or not to allow empty selection
     * @return this (for method chaining)
     * @see AbstractSelect#setNullSelectionAllowed(boolean)
     * @see AbstractSelect#setNullSelectionItemId(Object)
     * @see AbstractSelect#isNullSelectionAllowed()
     */
    public S withNullSelectionAllowed(boolean nullSelectionAllowed);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the item id that represents null value of this select.
     *
     * <p>
     * Data interface does not support nulls as item ids. Selecting the item
     * identified by this id is the same as selecting no items at all. This
     * setting only affects the single select mode.
     * </p>
     *
     * @param nullSelectionItemId the nullSelectionItemId to set.
     * @return this (for method chaining)
     * @see AbstractSelect#setNullSelectionItemId(java.lang.Object)
     * @see AbstractSelect#getNullSelectionItemId()
     * @see AbstractSelect#isSelected(Object)
     * @see AbstractSelect#select(Object)
     */
    public S withNullSelectionItemId(Object nullSelectionItemId);

}
