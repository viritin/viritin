package org.vaadin.viritin.fields;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.List;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.button.MButton;

/**
 * A field suitable for editing collection of referenced objects tied to parent
 * object only. E.g. OneToMany/ElementCollection fields in JPA world.
 * <p>
 * Some features/restrictions:
 * <ul>
 * <li>The field is valid when all elements are valid.
 * <li>The field is always non buffered
 * <li>The element type needs to have an empty parameter constructor or user
 * must provide an Instantiator.
 * </ul>
 *
 * Elements in the edited collection are modified with BeanFieldGroup. Fields
 * should defined in a class. A simple usage example for editing
 * List&gt;Address&lt; adresses:
 * <pre><code>
 *  public static class AddressRow {
 *      EnumSelect type = new EnumSelect();
 *      MTextField street = new MTextField();
 *      MTextField city = new MTextField();
 *      MTextField zipCode = new MTextField();
 *  }
 *
 *  public static class PersonForm&lt;Person&gt; extends AbstractForm {
 *      private final ElementCollectionField&lt;Address&gt; addresses
 *              = new ElementCollectionField&lt;Address&gt;(Address.class,
 *                      AddressRow.class).withCaption("Addressess");
 *
 * </code></pre>
 *
 * <p>
 * By default the field always contains an empty instance to create new rows. If
 * instances are added with some other method (or UI shouldn't add them at all),
 * you can configure this with setAllowNewItems. Deletions can be configured
 * with setAllowRemovingItems.
 * <p>
 * If developer needs to do some additional logic during element
 * addition/removal, one can subscribe to related events using
 * addElementAddedListener/addElementRemovedListener.
 *
 *
 * @author Matti Tahvonen
 * @param <ET> The type in the entity collection. The type must have empty
 * parameter constructor or you have to provide Instantiator.
 *
 */
public class ElementCollectionField<ET> extends AbstractElementCollection<ET> {

    List<ET> items = new ArrayList<ET>();

    boolean inited = false;

    GridLayout layout = new GridLayout();

    public ElementCollectionField(Class<ET> elementType,
            Class<?> formType) {
        super(elementType, formType);
    }

    public ElementCollectionField(Class<ET> elementType, Instantiator i,
            Class<?> formType) {
        super(elementType, i, formType);
    }

    @Override
    public void addInternalElement(final ET v) {
        ensureInited();
        items.add(v);
        MBeanFieldGroup<ET> fg = getFieldGroupFor(v);
        for (Object property : getVisibleProperties()) {
            Component c = fg.getField(property);
            layout.addComponent(c);
            layout.setComponentAlignment(c, Alignment.MIDDLE_LEFT);
        }
        if (isAllowRemovingItems()) {
            layout.addComponent(new MButton(FontAwesome.TRASH_O).withListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            removeElement(v);
                        }
                    }).withStyleName(ValoTheme.BUTTON_ICON_ONLY));
        }
        if(!isAllowEditItems())
        {
           fg.setReadOnly(true);
        }
    }

    @Override
    public void removeInternalElement(ET v) {
        int index = items.indexOf(v);
        items.remove(index);
        int row = index + 1;
        layout.removeRow(row);
    }

    @Override
    public GridLayout getLayout() {
        return layout;
    }

    @Override
    public void setPersisted(ET v, boolean persisted) {
        int row = items.indexOf(v) + 1;
        Button c = (Button) layout.getComponent(getVisibleProperties().size(),
                row);
        if (persisted) {
            c.setDescription(getDeleteElementDescription());
        } else {
            for (int i = 0; i < getVisibleProperties().size(); i++) {
                try {
                    AbstractField f = (AbstractField) (Field) layout.
                            getComponent(i,
                                    row);
                    f.setValidationVisible(false);
                } catch (Exception e) {

                }
            }
            c.setDescription(getDisabledDeleteElementDescription());
        }
        c.setEnabled(persisted);
    }

    private void ensureInited() {
        if (!inited) {
            layout.setSpacing(true);
            int columns = getVisibleProperties().size();
            if (isAllowRemovingItems()) {
                columns++;
            }
            layout.setColumns(columns);
            for (Object property : getVisibleProperties()) {
                Label header = new Label(getPropertyHeader(property.
                        toString()));
                header.setWidthUndefined();
                layout.addComponent(header);
            }
            if (isAllowRemovingItems()) {
                // leave last header slot empty, "actions" colunn
                layout.newLine();
            }
            inited = true;
        }
    }

    @Override
    public void clear() {
        if (inited) {
            items.clear();
            int rows = inited ? 1 : 0;
            while (layout.getRows() > rows) {
                layout.removeRow(rows);
            }
        }

    }

    public String getDisabledDeleteElementDescription() {
        return disabledDeleteThisElementDescription;
    }

    public void setDisabledDeleteThisElementDescription(
            String disabledDeleteThisElementDescription) {
        this.disabledDeleteThisElementDescription = disabledDeleteThisElementDescription;
    }

    private String disabledDeleteThisElementDescription = "Fill this row to add a new element, currently ignored";

    public String getDeleteElementDescription() {
        return deleteThisElementDescription;
    }

    private String deleteThisElementDescription = "Delete this element";

    public void setDeleteThisElementDescription(
            String deleteThisElementDescription) {
        this.deleteThisElementDescription = deleteThisElementDescription;
    }

    @Override
    public void onElementAdded() {
        if (isAllowNewItems()) {
            newInstance = createInstance();
            addInternalElement(newInstance);
            setPersisted(newInstance, false);
        }
    }

    @Override
    public ElementCollectionField<ET> setPropertyHeader(String propertyName,
            String propertyHeader) {
        super.setPropertyHeader(propertyName, propertyHeader);
        return this;
    }

    @Override
    public ElementCollectionField<ET> setVisibleProperties(
            List<String> properties, List<String> propertyHeaders) {
        super.setVisibleProperties(properties, propertyHeaders);
        return this;
    }

    @Override
    public ElementCollectionField<ET> setVisibleProperties(
            List<String> properties) {
        super.setVisibleProperties(properties);
        return this;
    }

    @Override
    public ElementCollectionField<ET> setAllowNewElements(
            boolean allowNewItems) {
        super.setAllowNewElements(allowNewItems);
        return this;
    }

    @Override
    public ElementCollectionField<ET> setAllowRemovingItems(
            boolean allowRemovingItems) {
        super.setAllowRemovingItems(allowRemovingItems);
        return this;
    }

    @Override
    public ElementCollectionField<ET> withCaption(String caption) {
        super.withCaption(caption);
        return this;
    }

    @Override
    public ElementCollectionField<ET> removeElementRemovedListener(
            ElementRemovedListener listener) {
        super.removeElementRemovedListener(listener);
        return this;
    }

    @Override
    public ElementCollectionField<ET> addElementRemovedListener(
            ElementRemovedListener<ET> listener) {
        super.addElementRemovedListener(listener);
        return this;
    }

    @Override
    public ElementCollectionField<ET> removeElementAddedListener(
            ElementAddedListener listener) {
        super.removeElementAddedListener(listener);
        return this;
    }

    @Override
    public ElementCollectionField<ET> addElementAddedListener(
            ElementAddedListener<ET> listener) {
        super.addElementAddedListener(listener);
        return this;
    }


    /**
     * Expands the column with given property id
     * @param propertyId the id of column that should be expanded in the UI
     * @return the element collection field
     */
    public ElementCollectionField<ET> expand(String... propertyId) {
        for (String propertyId1 : propertyId) {
            int index = getVisibleProperties().indexOf(propertyId1);
            if(index == -1) {
                throw new IllegalArgumentException("The expanded property must available");
            }
            layout.setColumnExpandRatio(index, 1);
        }
        if(layout.getWidth() == -1) {
            layout.setWidth(100, Unit.PERCENTAGE);
        }
        // TODO should also make width of elements automatically 100%, both
        // existing and added, now obsolete config needed for row model
        return this;
    }

    public ElementCollectionField<ET> withFullWidth() {
        setWidth(100, Unit.PERCENTAGE);
        return this;
    }

}
