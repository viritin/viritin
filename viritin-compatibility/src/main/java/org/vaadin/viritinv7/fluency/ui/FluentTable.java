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
package org.vaadin.viritinv7.fluency.ui;

import java.util.Collection;

import org.vaadin.viritin.fluency.event.FluentAction;
import org.vaadin.viritin.fluency.ui.FluentHasComponents;
import org.vaadin.viritinv7.fluency.data.FluentContainer;
import org.vaadin.viritinv7.fluency.event.FluentItemClickEvent;

import com.vaadin.event.dd.DropHandler;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.v7.data.Container;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.shared.ui.table.CollapseMenuContent;
import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.v7.ui.DefaultFieldFactory;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.Table.Align;
import com.vaadin.v7.ui.Table.ColumnGenerator;
import com.vaadin.v7.ui.TableFieldFactory;

/**
 * A {@link Table} complemented by fluent setters.
 *
 * @author Max Schuster
 * @param <S> Self-referential generic type
 * @see Table
 */
public interface FluentTable<S extends FluentTable<S>> extends
        FluentAbstractSelect<S>, FluentAction.FluentContainer<S>,
        FluentContainer.FluentOrdered<S>, FluentContainer.FluentSortable<S>,
        FluentItemClickEvent.FluentItemClickNotifier<S>, FluentHasComponents<S>,
        FluentHasChildMeasurementHint<S>, FluentContainer.Viewer {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the array of visible column property id:s.
     *
     * <p>
     * The columns are show in the order of their appearance in this array.
     * </p>
     *
     * @param visibleColumns the Array of shown property ids.
     * @return this (for method chaining)
     * @see Table#setVisibleColumns(java.lang.Object...)
     */
    public S withVisibleColumns(Object... visibleColumns);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the headers of the columns.
     *
     * <p>
     * The headers match the property id:s given by the set visible column
     * headers. The table must be set in either
     * {@link Table#COLUMN_HEADER_MODE_EXPLICIT} or
     * {@link Table#COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID} mode to show the
     * headers. In the defaults mode any nulls in the headers array are replaced
     * with id.toString() outputs when rendering.
     * </p>
     *
     * @param columnHeaders the Array of column headers that match the
     * {@link Table#getVisibleColumns()} method.
     * @return this (for method chaining)
     * @see Table#setColumnHeaders(java.lang.String...)
     */
    public S withColumnHeaders(String... columnHeaders);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the icons of the columns.
     *
     * <p>
     * The icons in headers match the property id:s given by the set visible
     * column headers. The table must be set in either
     * {@link Table#COLUMN_HEADER_MODE_EXPLICIT} or
     * {@link Table#COLUMN_HEADER_MODE_EXPLICIT_DEFAULTS_ID} mode to show the headers
     * with icons.
     * </p>
     *
     * @param columnIcons the Array of icons that match the
     * {@link Table#getVisibleColumns()}.
     * @return this (for method chaining)
     * @see Table#setColumnIcons(com.vaadin.server.Resource...)
     */
    public S withColumnIcons(Resource... columnIcons);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the column alignments.
     *
     * <p>
     * The amount of items in the array must match the amount of properties
     * identified by {@link Table#getVisibleColumns()}. The possible values for the
     * alignments include:
     * </p>
     * <ul>
     * <li>{@link Align#LEFT}: Left alignment</li>
     * <li>{@link Align#CENTER}: Centered</li>
     * <li>{@link Align#RIGHT}: Right alignment</li>
     * </ul>
     * <p>
     * The alignments default to {@link Align#LEFT}
     * </p>
     *
     * @param columnAlignments the Column alignments array.
     * @return this (for method chaining)
     * @see Table#setColumnAlignments(Table.Align...)
     */
    public S withColumnAlignments(Table.Align... columnAlignments);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets columns width (in pixels). Theme may not necessarily respect very
     * small or very big values. Setting width to -1 (default) means that theme
     * will make decision of width.
     *
     * <p>
     * Column can either have a fixed width or expand ratio. The latter one set
     * is used. See @link {@link Table#setColumnExpandRatio(Object, float)}.
     *
     * @param propertyId columns property id
     * @param width width to be reserved for columns content
     * @return this (for method chaining)
     * @see Table#setColumnWidth(java.lang.Object, int)
     */
    public S withColumnWidth(Object propertyId, int width);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the column expand ratio for given column.
     * <p>
     * Expand ratios can be defined to customize the way how excess space is
     * divided among columns. Table can have excess space if it has its width
     * defined and there is horizontally more space than columns consume
     * naturally. Excess space is the space that is not used by columns with
     * explicit width (see {@link Table#setColumnWidth(Object, int)}) or with natural
     * width (no width nor expand ratio).
     *
     * <p>
     * By default (without expand ratios) the excess space is divided
     * proportionally to columns natural widths.
     *
     * <p>
     * Only expand ratios of visible columns are used in final calculations.
     *
     * <p>
     * Column can either have a fixed width or expand ratio. The latter one set
     * is used.
     *
     * <p>
     * A column with expand ratio is considered to be minimum width by default
     * (if no excess space exists). The minimum width is defined by terminal
     * implementation.
     *
     * <p>
     * If terminal implementation supports re-sizable columns the column becomes
     * fixed width column if users resizes the column.
     *
     * @param propertyId columns property id
     * @param expandRatio the expandRatio used to divide excess space for this
     * column
     * @return this (for method chaining)
     * @see Table#setColumnExpandRatio(java.lang.Object, float)
     */
    public S withColumnExpandRatio(Object propertyId, float expandRatio);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the page length.
     *
     * <p>
     * Setting page length 0 disables paging. The page length defaults to 15.
     * </p>
     *
     * <p>
     * If Table has height set ({@link Table#setHeight(float, Unit)} ) the client
     * side may update the page length automatically the correct value.
     * </p>
     *
     * @param pageLength the length of one page.
     * @return this (for method chaining)
     * @see Table#setPageLength(int)
     */
    public S withPageLength(int pageLength);

    // Javadoc copied form Vaadin Framework
    /**
     * This method adjusts a possible caching mechanism of table implementation.
     *
     * <p>
     * Table component may fetch and render some rows outside visible area. With
     * complex tables (for example containing layouts and components), the
     * client side may become unresponsive. Setting the value lower, UI will
     * become more responsive. With higher values scrolling in client will hit
     * server less frequently.
     *
     * <p>
     * The amount of cached rows will be cacheRate multiplied with pageLength (
     * {@link Table#setPageLength(int)} both below and above visible area..
     *
     * @param cacheRate a value over 0 (fastest rendering time). Higher value
     * will cache more rows on server (smoother scrolling). Default value is 2.
     * @return this (for method chaining)
     * @see Table#setCacheRate(double)
     */
    public S withCacheRate(double cacheRate);

    // Javadoc copied form Vaadin Framework
    /**
     * Setter for property currentPageFirstItemId.
     *
     * @param currentPageFirstItemId the New value of property
     * currentPageFirstItemId.
     * @return this (for method chaining)
     * @see Table#setCurrentPageFirstItemId(java.lang.Object)
     */
    public S withCurrentPageFirstItemId(Object currentPageFirstItemId);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the icon Resource for the specified column.
     * <p>
     * Throws IllegalArgumentException if the specified column is not visible.
     * </p>
     *
     * @param propertyId the propertyId identifying the column.
     * @param icon the icon Resource to set.
     * @return this (for method chaining)
     * @see Table#setColumnIcon(java.lang.Object, com.vaadin.server.Resource)
     */
    public S withColumnIcon(Object propertyId, Resource icon);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the column header for the specified column;
     *
     * @param propertyId the propertyId identifying the column.
     * @param header the header to set.
     * @return this (for method chaining)
     * @see Table#setColumnHeader(java.lang.Object, java.lang.String)
     */
    public S withColumnHeader(Object propertyId, String header);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the specified column's alignment.
     *
     * <p>
     * Throws IllegalArgumentException if the alignment is not one of the
     * following: {@link Align#LEFT}, {@link Align#CENTER} or
     * {@link Align#RIGHT}
     * </p>
     *
     * @param propertyId the propertyID identifying the column.
     * @param alignment the desired alignment.
     * @return this (for method chaining)
     * @see Table#setColumnAlignment(java.lang.Object, Table.Align)
     */
    public S withColumnAlignment(Object propertyId, Table.Align alignment);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets whether the specified column is collapsed or not.
     *
     * @return this (for method chaining)
     * @param propertyId the propertyID identifying the column.
     * @param collapsed the desired collapsedness.
     * @throws IllegalStateException if column collapsing is not allowed
     * @throws IllegalArgumentException if the property id does not exist
     * @see Table#setColumnCollapsed(java.lang.Object, boolean)
     */
    public S withColumnCollapsed(Object propertyId, boolean collapsed) throws IllegalStateException;

    // Javadoc copied form Vaadin Framework
    /**
     * Sets whether column collapsing is allowed or not.
     *
     * @param collapsingAllowed specifies whether column collapsing is allowed.
     * @return this (for method chaining)
     * @see Table#setColumnCollapsingAllowed(boolean)
     */
    public S withColumnCollapsingAllowed(boolean collapsingAllowed);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets whether the given column is collapsible. Note that collapsible
     * columns can only be actually collapsed (via UI or with
     * {@link Table#setColumnCollapsed(Object, boolean) setColumnCollapsed()}) if
     * {@link Table#isColumnCollapsingAllowed()} is true. By default all columns are
     * collapsible.
     *
     * @param propertyId the propertyID identifying the column.
     * @param collapsible true if the column should be collapsible, false
     * otherwise.
     * @return this (for method chaining)
     * @see Table#setColumnCollapsible(java.lang.Object, boolean)
     */
    public S withColumnCollapsible(Object propertyId, boolean collapsible);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets whether column reordering is allowed or not.
     *
     * @param columnReorderingAllowed specifies whether column reordering is
     * allowed.
     * @return this (for method chaining)
     * @see Table#setColumnReorderingAllowed(boolean)
     */
    public S withColumnReorderingAllowed(boolean columnReorderingAllowed);

    // Javadoc copied form Vaadin Framework
    /**
     * Setter for property currentPageFirstItem.
     *
     * @param newIndex the New value of property currentPageFirstItem.
     * @return this (for method chaining)
     * @see Table#setCurrentPageFirstItemIndex(int)
     */
    public S withCurrentPageFirstItemIndex(int newIndex);

    // Javadoc copied form Vaadin Framework
    /**
     * Setter for property selectable.
     *
     * <p>
     * The table is not selectable until it's explicitly set as selectable via
     * this method or alternatively at least one {@link ValueChangeListener} is
     * added.
     * </p>
     *
     * @param selectable the New value of property selectable.
     * @return this (for method chaining)
     * @see Table#setSelectable(boolean)
     */
    public S withSelectable(boolean selectable);

    // Javadoc copied form Vaadin Framework
    /**
     * Setter for property columnHeaderMode.
     *
     * @param columnHeaderMode the New value of property columnHeaderMode.
     * @return this (for method chaining)
     * @see Table#setColumnHeaderMode(Table.ColumnHeaderMode)
     */
    public S withColumnHeaderMode(Table.ColumnHeaderMode columnHeaderMode);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the row header mode.
     * <p>
     * The mode can be one of the following ones:
     * </p>
     * <ul>
     * <li>{@link Table#ROW_HEADER_MODE_HIDDEN}: The row captions are hidden.</li>
     * <li>{@link Table#ROW_HEADER_MODE_ID}: Items Id-objects <code>toString()</code>
     * is used as row caption.
     * <li>{@link Table#ROW_HEADER_MODE_ITEM}: Item-objects <code>toString()</code>
     * is used as row caption.
     * <li>{@link Table#ROW_HEADER_MODE_PROPERTY}: Property set with
     * {@link Table#setItemCaptionPropertyId(Object)} is used as row header.
     * <li>{@link Table#ROW_HEADER_MODE_EXPLICIT_DEFAULTS_ID}: Items Id-objects
     * <code>toString()</code> is used as row header. If caption is explicitly
     * specified, it overrides the id-caption.
     * <li>{@link Table#ROW_HEADER_MODE_EXPLICIT}: The row headers must be explicitly
     * specified.</li>
     * <li>{@link Table#ROW_HEADER_MODE_INDEX}: The index of the item is used as row
     * caption. The index mode can only be used with the containers implementing
     * <code>Container.Indexed</code> interface.</li>
     * </ul>
     * <p>
     * The default value is {@link Table#ROW_HEADER_MODE_HIDDEN}
     * </p>
     *
     * @param mode the One of the modes listed above.
     * @return this (for method chaining)
     * @see Table#setRowHeaderMode(Table.RowHeaderMode)
     */
    public S withRowHeaderMode(Table.RowHeaderMode mode);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the container data source and the columns that will be visible.
     * Columns are shown in the collection's iteration order.
     * <p>
     * Keeps propertyValueConverters if the corresponding id exists in the new
     * data source and is of a compatible type.
     * </p>
     *
     * @param newDataSource the new data source.
     * @param visibleIds IDs of the visible columns
     * @return this (for method chaining)
     * @see Table#setContainerDataSource(Container,
     * java.util.Collection)
     * @see Table#setContainerDataSource(Container)
     * @see Table#setVisibleColumns(Object[])
     * @see Table#setConverter(Object, Converter)
     */
    public S withContainerDataSource(Container newDataSource, Collection<?> visibleIds);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a new property to the table and show it as a visible column.
     *
     * @param propertyId the Id of the property
     * @param type the class of the property
     * @param defaultValue the default value given for all existing items
     * @param columnHeader the Explicit header of the column. If explicit header
     * is not needed, this should be set null.
     * @param columnIcon the Icon of the column. If icon is not needed, this
     * should be set null.
     * @param columnAlignment the Alignment of the column. Null implies align
     * left.
     * @return this (for method chaining)
     * @throws UnsupportedOperationException if the operation is not supported.
     * @see Container#addContainerProperty(Object, Class,
     * Object)
     * @see Table#addContainerProperty(java.lang.Object, java.lang.Class,
     * java.lang.Object, java.lang.String, Resource,
     * Table.Align)
     */
    public S withContainerProperty(Object propertyId, Class<?> type,
            Object defaultValue, String columnHeader, Resource columnIcon,
            Table.Align columnAlignment);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a generated column to the Table.
     * <p>
     * A generated column is a column that exists only in the Table, not as a
     * property in the underlying Container. It shows up just as a regular
     * column.
     * </p>
     * <p>
     * A generated column will override a property with the same id, so that the
     * generated column is shown instead of the column representing the
     * property. Note that getContainerProperty() will still get the real
     * property.
     * </p>
     * <p>
     * Table will not listen to value change events from properties overridden
     * by generated columns. If the content of your generated column depends on
     * properties that are not directly visible in the table, attach value
     * change listener to update the content on all depended properties.
     * Otherwise your UI might not get updated as expected.
     * </p>
     * <p>
     * Also note that getVisibleColumns() will return the generated columns,
     * while getContainerPropertyIds() will not.
     * </p>
     *
     * @param id the id of the column to be added
     * @param generatedColumn the {@link ColumnGenerator} to use for this column
     * @return this (for method chaining)
     * @see Table#addGeneratedColumn(java.lang.Object,
     * Table.ColumnGenerator)
     */
    public S withGeneratedColumn(Object id, Table.ColumnGenerator generatedColumn);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the TableFieldFactory that is used to create editor for table cells.
     *
     * The TableFieldFactory is only used if the Table is editable. By default
     * the DefaultFieldFactory is used.
     *
     * @param fieldFactory the field factory to set.
     * @return this (for method chaining)
     * @see Table#setTableFieldFactory(TableFieldFactory)
     * @see Table#isEditable()
     * @see DefaultFieldFactory
     */
    public S withTableFieldFactory(TableFieldFactory fieldFactory);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the editable property.
     *
     * If table is editable a editor of type Field is created for each table
     * cell. The assigned FieldFactory is used to create the instances.
     *
     * To provide custom editors for table cells create a class implementing the
     * FieldFactory interface, and assign it to table, and set the editable
     * property to true.
     *
     * @param editable true if table should be editable by user.
     * @return this (for method chaining)
     * @see Table#setEditable(boolean)
     * @see TableFieldFactory
     */
    public S withEditable(boolean editable);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the currently sorted column property id.
     *
     * @param propertyId the Container property id of the currently sorted
     * column.
     * @return this (for method chaining)
     * @see Table#setSortContainerPropertyId(java.lang.Object)
     */
    public S withSortContainerPropertyId(Object propertyId);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the table in ascending order.
     *
     * @param ascending <code>true</code> if ascending, <code>false</code> if
     * descending.
     * @return this (for method chaining)
     * @see Table#setSortAscending(boolean)
     */
    public S withSortAscending(boolean ascending);

    // Javadoc copied form Vaadin Framework
    /**
     * Enables or disables sorting.
     * <p>
     * Setting this to false disallows sorting by the user. It is still possible
     * to call {@link Table#sort()}.
     * </p>
     *
     * @param sortEnabled true to allow the user to sort the table, false to
     * disallow it
     * @return this (for method chaining)
     * @see Table#setSortEnabled(boolean)
     */
    public S withSortEnabled(boolean sortEnabled);

    // Javadoc copied form Vaadin Framework
    /**
     * Set cell style generator for Table.
     *
     * @param cellStyleGenerator New cell style generator or null to remove
     * generator.
     * @return this (for method chaining)
     * @see Table#setCellStyleGenerator(Table.CellStyleGenerator)
     */
    public S withCellStyleGenerator(Table.CellStyleGenerator cellStyleGenerator);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the drag start mode of the Table. Drag start mode controls how Table
     * behaves as a drag source.
     *
     * @param newDragMode The drag mode
     * @return this (for method chaining)
     * @see Table#setDragMode(Table.TableDragMode)
     */
    public S withDragMode(Table.TableDragMode newDragMode);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the drop handler of the table.
     *
     * @param dropHandler The drop handler of the table
     * @return this (for method chaining)
     * @see Table#setDropHandler(DropHandler)
     */
    public S withDropHandler(DropHandler dropHandler);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the behavior of how the multi-select mode should behave when the
     * table is both selectable and in multi-select mode.
     * <p>
     * Note, that on some clients the mode may not be respected. E.g. on touch
     * based devices CTRL/SHIFT base selection method is invalid, so touch based
     * browsers always use the {@link MultiSelectMode#SIMPLE}.
     *
     * @param mode The select mode of the table
     * @return this (for method chaining)
     * @see Table#setMultiSelectMode(MultiSelectMode)
     */
    public S withMultiSelectMode(MultiSelectMode mode);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a header click listener which handles the click events when the user
     * clicks on a column header cell in the Table.
     * <p>
     * The listener will receive events which contain information about which
     * column was clicked and some details about the mouse event.
     * </p>
     *
     * @param listener The handler which should handle the header click events.
     * @return this (for method chaining)
     * @see
     * Table#addHeaderClickListener(Table.HeaderClickListener)
     */
    public S withHeaderClickListener(Table.HeaderClickListener listener);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the column footer caption. The column footer caption is the text
     * displayed beneath the column if footers have been set visible.
     *
     * @param propertyId The properyId of the column
     *
     * @param footer The caption of the footer
     * @return this (for method chaining)
     * @see Table#setColumnFooter(java.lang.Object, java.lang.String)
     */
    public S withColumnFooter(Object propertyId, String footer);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the footer visible in the bottom of the table.
     * <p>
     * The footer can be used to add column related data like sums to the bottom
     * of the Table using setColumnFooter(Object propertyId, String footer).
     * </p>
     *
     * @param visible Should the footer be visible
     * @return this (for method chaining)
     * @see Table#setFooterVisible(boolean)
     */
    public S withFooterVisible(boolean visible);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a column resize listener to the Table. A column resize listener is
     * called when a user resizes a columns width.
     *
     * @param listener The listener to attach to the Table
     * @return this (for method chaining)
     * @see
     * Table#addColumnResizeListener(Table.ColumnResizeListener)
     */
    public S withColumnResizeListener(Table.ColumnResizeListener listener);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a column reorder listener to the Table. A column reorder listener is
     * called when a user reorders columns.
     *
     * @param listener The listener to attach to the Table
     * @return this (for method chaining)
     * @see
     * Table#addColumnReorderListener(Table.ColumnReorderListener)
     */
    public S withColumnReorderListener(Table.ColumnReorderListener listener);

    // Javadoc copied form Vaadin Framework
    /**
     * Adds a column collapse listener to the Table. A column collapse listener
     * is called when the collapsed state of a column changes.
     *
     * @param listener The listener to attach
     * @return this (for method chaining)
     * @see
     * Table#addColumnCollapseListener(Table.ColumnCollapseListener)
     */
    public S withColumnCollapseListener(Table.ColumnCollapseListener listener);

    // Javadoc copied form Vaadin Framework
    /**
     * Set the item description generator which generates tooltips for cells and
     * rows in the Table
     *
     * @param generator The generator to use or null to disable
     * @return this (for method chaining)
     * @see
     * Table#setItemDescriptionGenerator(AbstractSelect.ItemDescriptionGenerator)
     */
    public S withItemDescriptionGenerator(AbstractSelect.ItemDescriptionGenerator generator);

    // Javadoc copied form Vaadin Framework
    /**
     * Assigns a row generator to the table. The row generator will be able to
     * replace rows in the table when it is rendered.
     *
     * @param generator the new row generator
     * @return this (for method chaining)
     * @see Table#setRowGenerator(Table.RowGenerator)
     */
    public S withRowGenerator(Table.RowGenerator generator);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets a converter for a property id.
     * <p>
     * The converter is used to format the the data for the given property id
     * before displaying it in the table.
     * </p>
     *
     * @param propertyId The propertyId to format using the converter
     * @param converter The converter to use for the property id
     * @return this (for method chaining)
     * @see Table#setConverter(java.lang.Object, Converter)
     */
    public S withConverter(Object propertyId, Converter<String, ?> converter);

    // Javadoc copied form Vaadin Framework
    /**
     * Sets whether only collapsible columns should be shown to the user in the
     * column collapse menu. The default is
     * {@link CollapseMenuContent#ALL_COLUMNS}.
     *
     * @param content the desired collapsible menu content setting
     * @return this (for method chaining)
     * @see
     * Table#setCollapseMenuContent(CollapseMenuContent)
     */
    public S withCollapseMenuContent(CollapseMenuContent content);

}
