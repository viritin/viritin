package org.vaadin.viritin.fields;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.v7.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.v7.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.v7.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Selects a set of beans from a larger set of choices. Bit similar UI component
 * as "TwinColSelect", but available options are in a combobox for easy picking,
 * and selected entries are displayed in a Table. Should work with virtually any
 * collection type - not just with sets, but same bean can be only once in the
 * collection. If a null is passed for the field value, it is "converted" into
 * and empty Set or list List.
 * <p>
 * The component can be configured to create new entities as well on demand,
 * either by hitting enter with ComboBox or with a new entry form, or combined.
 *
 * @param <ET> the type of items in from which the selection is build
 */
@SuppressWarnings({"rawtypes", "serial", "unchecked"})
public class SubSetSelector<ET> extends CustomField<Collection> implements AbstractForm.SavedHandler<ET>, AbstractForm.ResetHandler<ET> {

    private Class<?> type;
    private TypedSelect<ET> cb;
    private MTable<ET> table;
    private Collection selected;
    private Button newInstanceBtn;
    private final Class<ET> elementType;
    private MHorizontalLayout toprow;
    private MVerticalLayout verticalLayout;
    private AbstractForm<ET> newInstanceForm;
    private List<ET> availableOptions;
    private int limit = Integer.MAX_VALUE;

    public SubSetSelector(Class<ET> elementType) {
        this.elementType = elementType;
        cb = new TypedSelect<>(elementType).withSelectType(ComboBox.class);
        table = new MTable<>(elementType).withFullWidth();
        setHeight("300px");
        toprow = new MHorizontalLayout(cb);
        verticalLayout = new MVerticalLayout(toprow).expand(table);

        table.setPageLength(5);
        table.withGeneratedColumn("Remove", new MTable.SimpleColumnGenerator<ET>() {
            @Override
            public Object generate(final ET entity) {
                return getToolColumnContent(entity);
            }
        });
        table.setColumnHeader("Remove", "");
        cb.setInputPrompt("Add to selection...");
        cb.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(
                    com.vaadin.v7.data.Property.ValueChangeEvent event) {
                if (event.getProperty().getValue() != null) {
                    Object pojo = event.getProperty().getValue();
                    cb.getBic().removeItem(pojo);
                    cb.setValue(null);
                    table.addItem(pojo);
                    selected.add(pojo);
                    cb.setEnabled(selected.size() < limit);
                    // fire value change
                    fireValueChange(true);
                }
            }
        });

    }

    /**
     * Generates the tool cell content in the listing of selected items. By
     * default contains button to remove selection. Overridden implementation
     * can add other stuff there as well, like edit button.
     *
     * @param entity the entity for which the cell content is created
     * @return the content (String or Component)
     */
    protected Object getToolColumnContent(final ET entity) {
        Button button = new Button(FontAwesome.MINUS);
        button.setDescription("Removes the selection from the list");
        button.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        button.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                removeSelectedOption(entity);
            }
        });
        button.setStyleName(ValoTheme.BUTTON_SMALL);
        return button;

    }

    /**
     * @param entity the entity to be removed from the selection
     */
    public void removeSelectedOption(ET entity) {
        cb.addOption(entity);
        table.removeItem(entity);
        selected.remove(entity);
        cb.setEnabled(selected.size() < limit);
        // fire value change
        fireValueChange(true);
    }

    public String getAddInputPrompt() {
        return ((ComboBox) cb.getSelect()).getInputPrompt();
    }

    public void setAddInputPrompt(String inputPrompt) {
        cb.setInputPrompt(inputPrompt);
    }

    /**
     * Sets a form which can be used to add new items to the selection. A button
     * to add new instances is displayed if this method is called.
     *
     * @param newInstanceForm the form
     */
    public void setNewInstanceForm(AbstractForm<ET> newInstanceForm) {
        this.newInstanceForm = newInstanceForm;
        if (newInstanceForm != null) {
            if (newInstanceBtn == null) {
                newInstanceBtn = new MButton(FontAwesome.PLUS).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
                newInstanceBtn.addClickListener(new Button.ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {
                        addEntity(null);
                    }
                });
                toprow.add(newInstanceBtn);
            }
        } else if (newInstanceBtn != null) {
            toprow.removeComponent(newInstanceBtn);
            newInstanceBtn = null;
        }
    }

    public AbstractForm<ET> getNewInstanceForm() {
        return newInstanceForm;
    }

    protected String getDeleteButtonCaption() {
        return "-";
    }

    /**
     * @param headers the headers to be used for the table containing the
     * selected items in the display order of the columns
     * @see Table#setColumnHeaders(String[])
     */
    public void setColumnHeaders(String... headers) {
        table.setColumnHeaders(headers);
    }

    /**
     * @param propertyId the id of the property whose columen header is to be
     * set
     * @param header the columen header
     * @see Table#setColumnHeader(Object, String)
     */
    public void setColumnHeader(Object propertyId, String header) {
        table.setColumnHeader(propertyId, header);
    }

    /**
     * @return the reference to the Table used by this field internally.
     * Modifying this object directly might cause odd behavior.
     */
    public MTable getTable() {
        return table;
    }

    protected void addEntity(String stringInput) {
        final ET newInstance = instantiateOption(stringInput);

        if (newInstanceForm != null) {
            String caption = "Add new " + elementType.getSimpleName();
            newInstanceForm.setEntity(newInstance);
            newInstanceForm.setSavedHandler(this);
            newInstanceForm.setResetHandler(this);
            Window w = newInstanceForm.openInModalPopup();
            w.setWidth("70%");
            w.setCaption(caption);
        } else {
            onSave(newInstance);
        }
    }

    protected ET instantiateOption(String stringInput) {
        try {
            return elementType.getConstructor().newInstance();
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Class<Collection> getType() {
        return (Class<Collection>) type;
    }

    /**
     * @param options the list of options from which the sub set is selected
     * @return this
     */
    public SubSetSelector<ET> setOptions(List<ET> options) {
        availableOptions = options;
        cb.setOptions(new ArrayList<>(options));
        return this;
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        if (newDataSource != null) {
            type = newDataSource.getType();
        }
        super.setPropertyDataSource(newDataSource);
    }

    @Override
    protected void setInternalValue(Collection newValue) {
        selected = newValue;
        if (selected == null) {
            Class<Collection> clazz = getType();
            if (clazz != null && List.class.isAssignableFrom(clazz)) {
                selected = new ArrayList();
            } else {
                selected = new HashSet();
            }
            setValue(selected);
            return;
        }
        final ArrayList<ET> arrayList = new ArrayList<>(availableOptions);
        arrayList.removeAll(selected);
        cb.setOptions(arrayList);
        cb.getBic().fireItemSetChange();
        cb.setEnabled(selected.size() < limit);
        table.setBeans(new ArrayList(selected));
        super.setInternalValue(newValue);
    }

    @Override
    protected void fireValueChange(boolean repaintIsNotNeeded) {
        super.fireValueChange(repaintIsNotNeeded);
    }

    public void setVisibleProperties(String... visible) {
        List<String> a = new ArrayList(Arrays.asList(visible));
        a.add("Remove");
        table.withProperties(a);
        table.setColumnHeader("Remove", "");
    }

    public String getNewEntityCaption() {
        return newInstanceBtn.getCaption();
    }

    public void setNewEntityCaption(String newEntityCaption) {
        newInstanceBtn.setCaption(newEntityCaption);
    }

    @Override
    protected Component initContent() {
        return verticalLayout;
    }

    public void setCaptionGenerator(CaptionGenerator<ET> cg) {
        cb.setCaptionGenerator(cg);
    }

    @Override
    public void onSave(ET entity) {
        // TODO, figure out something here, needs a listener/handler
        // getProvider().persist(elementType, newInstance);
        table.addItem(entity);
        selected.add(entity);
        /*
         * Here we check the table for limit because the added entity could be equal to another added previously.
         * Since the table has a list container and selected has a map, they do not have the same behavior for equality.
         */
        cb.setEnabled(table.size() < limit);
        if (newInstanceForm != null) {
            newInstanceForm.closePopup();
        }
        // fire value change
        fireValueChange(true);

    }

    @Override
    public void onReset(ET entity) {
        newInstanceForm.closePopup();
    }

    /**
     * With this method users can be allowed to add new entities directly via
     * the combobox used to select entities from the existing set.
     * <p>
     * Developer can decide what to do with the input string, by overriding the
     * instantiateOption method. Also override onSave method if you need to do
     * something specific with new entities (e.g. save to persistency context
     * and pass the persisted instance to the default action that adds it to
     * select and available options).
     * <p>
     * If a newInstanceForm is set, the created the entity is then shown for
     * further editing in the form.
     *
     * @param allowAddingNewItems true if hitting enter without suggestions
     * should add a new item.
     */
    public void setNewItemsAllowed(boolean allowAddingNewItems) {
        cb.getSelect().setNewItemsAllowed(allowAddingNewItems);
        if (allowAddingNewItems) {
            cb.getSelect().setNewItemHandler(new AbstractSelect.NewItemHandler() {
                @Override
                public void addNewItem(String s) {
                    addEntity(s);
                }
            });
        } else {
            cb.getSelect().setNewItemHandler(null);
        }
    }

    public int getLimit() {
        return limit;
    }

    /**
     * Sets the limit of elements added to the SubSetSelector. Setting the limit
     * or adding elements will trigger the combo box availability.
     *
     * @param limit the maximum number of selected elements (collection size)
     */
    public void setLimit(int limit) {
        if (limit < 0) {
            this.limit = Integer.MAX_VALUE;
        } else {
            this.limit = limit;
        }
        cb.setEnabled(selected.size() < limit);
    }

}
