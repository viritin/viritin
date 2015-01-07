package org.vaadin.maddon.fields;

import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.ReflectTools;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.vaadin.maddon.BeanBinder;
import org.vaadin.maddon.MBeanFieldGroup;
import org.vaadin.maddon.MBeanFieldGroup.FieldGroupListener;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * A field suitable for editing collection of referenced objects tied to parent
 * object only. E.g. OneToMany/ElementCollection fields in JPA world.
 * <p>
 * Some features/restrictions:
 * <ul>
 * <li>The field is valid when all elements are valid.
 * <li>The field is always non buffered
 * <li>The element type needs to have an empty paremeter constructor or user
 * must provide an Instantiator.
 * </ul>
 *
 * <p>
 * By default the field always contains an empty instance to create new rows. In
 * upcoming versions this can be configured to contain a separate add button or
 * disable adding altogether.
 *
 * @author Matti Tahvonen
 * @param <ET> The type in the entity collection. The type must have empty
 * paremeter constructor or you have to provide Instantiator.
 */
public class InlineEditableCollection<ET> extends CustomField<Collection> {

    public static class ElementAddedEvent<ET> extends Component.Event {

        private final ET element;

        public ElementAddedEvent(InlineEditableCollection source, ET element) {
            super(source);
            this.element = element;
        }

        public ET getElement() {
            return element;
        }

    }

    public static class ElementRemovedEvent<ET> extends Component.Event {

        private final ET element;

        public ElementRemovedEvent(InlineEditableCollection source, ET element) {
            super(source);
            this.element = element;
        }

        public ET getElement() {
            return element;
        }

    }

    public interface ElementAddedListener<ET> {

        void elementAdded(ElementAddedEvent<ET> e);
    }

    public interface ElementRemovedListener<ET> {

        void elementRemoved(ElementRemovedEvent<ET> e);
    }

    private static final Method addedMethod;
    private static final Method removedMethod;

    static {
        addedMethod = ReflectTools.findMethod(ElementAddedListener.class,
                "elementAdded", ElementAddedEvent.class);
        removedMethod = ReflectTools.findMethod(ElementRemovedListener.class,
                "elementRemoved", ElementRemovedEvent.class);
    }

    public InlineEditableCollection<ET> addElementAddedListener(
            ElementAddedListener<ET> listener) {
        addListener(ElementAddedEvent.class, listener, addedMethod);
        return this;
    }

    public InlineEditableCollection<ET> removeElementAddedListener(
            ElementAddedListener listener) {
        removeListener(ElementAddedEvent.class, listener, addedMethod);
        return this;
    }

    public InlineEditableCollection<ET> addElementRemovedListener(
            ElementRemovedListener<ET> listener) {
        addListener(ElementRemovedEvent.class, listener, removedMethod);
        return this;
    }

    public InlineEditableCollection<ET> removeElementRemovedListener(
            ElementRemovedListener listener) {
        removeListener(ElementRemovedEvent.class, listener, removedMethod);
        return this;
    }

    // TODO consider extracting a super class and just have separate implementations
    private UIStrategy uiStrategy = UIStrategy.DEFAULT;

    public enum UIStrategy {

        /**
         * GridLayout based UI.
         */
        DEFAULT,
        /**
         * Table based UI
         */
        TABLE
    }

    private Instantiator<ET> instantiator;
    private Instantiator<?> editorInstantiator;

    private final Class<ET> elementType;

    private final Class<?> editorType;
    private Strategy strategy;
    private ET newInstance;

    FieldGroupListener fieldGroupListener = new FieldGroupListener() {

        @Override
        public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
            if (beanFieldGroup.getItemDataSource().getBean() == newInstance) {
                if (!getFieldGroupFor(newInstance).isValid()) {
                    return;
                }
                getAndEnsureValue().add(newInstance);
                fireEvent(new ElementAddedEvent(InlineEditableCollection.this,
                        newInstance));
                getStrategy().setPersisted(newInstance, true);
                getStrategy().onElementAdded();
            }
            // TODO could optimize for only repainting on changed validity
            fireValueChange(false);
        }
    };
    private List<String> visibleProperties;
    private boolean allowNewItems = true;

    public InlineEditableCollection<ET> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        super.validate();
        for (Object o : getValue()) {
            for (Field f : getFieldGroupFor((ET) o).getFields()) {
                f.validate();
            }
        }
    }

    private Collection<ET> getAndEnsureValue() {
        Collection<ET> value = getValue();
        if (value == null) {
            if (getPropertyDataSource() == null) {
                // this should never happen :-)
                return new HashSet();
            }
            Class fieldType = getPropertyDataSource().getType();
            if (fieldType.isInterface()) {
                if (fieldType == List.class) {
                    value = new ArrayList();
                } else { // Set
                    value = new HashSet();
                }
            } else {
                try {
                    value = (Collection) fieldType.newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException(
                            "Could not instantiate the used colleciton type", ex);
                }
            }
            getPropertyDataSource().setValue(value);
        }
        return value;
    }

    public InlineEditableCollection<ET> setAllowNewElements(
            boolean allowNewItems) {
        this.allowNewItems = allowNewItems;
        return this;
    }

    public interface Instantiator<ET> {

        ET create();
    }

    public InlineEditableCollection(Class<ET> elementType,
            Class<?> formType) {
        this.elementType = elementType;
        this.editorType = formType;
    }

    public InlineEditableCollection(Class<ET> elementType, Instantiator i,
            Class<?> formType) {
        this.elementType = elementType;
        this.instantiator = i;
        this.editorType = formType;
    }

    protected ET createInstance() {
        if (instantiator != null) {
            return instantiator.create();
        } else {
            try {
                return elementType.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected Object createEditorInstance() {
        if (editorInstantiator != null) {
            return editorInstantiator.create();
        } else {
            try {
                return editorType.newInstance();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private final Map<ET, MBeanFieldGroup<ET>> pojoToEditor = new HashMap<ET, MBeanFieldGroup<ET>>();

    protected final MBeanFieldGroup<ET> getFieldGroupFor(ET pojo) {
        MBeanFieldGroup editor = pojoToEditor.get(pojo);
        if (editor == null) {
            Object o = createEditorInstance();
            editor = BeanBinder.bind(pojo, o).withEagerValidation(
                    fieldGroupListener);
            // TODO listen for all changes for proper modified/validity changes
            pojoToEditor.put(pojo, editor);
        }
        return editor;
    }

    public void addElement(ET instance) {
        getAndEnsureValue().add(instance);
        strategy.addElement(instance);
        fireValueChange(false);
        fireEvent(new ElementAddedEvent<ET>(this, instance));
    }

    public void removeElement(ET elemnentToBeRemoved) {
        strategy.removeElement(elemnentToBeRemoved);
        getAndEnsureValue().remove(elemnentToBeRemoved);
        fireValueChange(false);
        fireEvent(new ElementRemovedEvent<ET>(this, elemnentToBeRemoved));
    }

    public InlineEditableCollection<ET> setVisibleProperties(
            List<String> properties) {
        visibleProperties = properties;
        return this;
    }

    public InlineEditableCollection<ET> setVisibleProperties(
            List<String> properties, List<String> propertyHeaders) {
        visibleProperties = properties;
        Iterator<String> it = propertyHeaders.iterator();
        for (String prop : visibleProperties) {
            setPropertyHeader(prop, it.next());
        }
        return this;
    }

    private final Map<String, String> propertyToHeader = new HashMap<String, String>();

    public InlineEditableCollection<ET> setPropertyHeader(String propertyName,
            String propertyHeader) {
        propertyToHeader.put(propertyName, propertyHeader);
        return this;
    }

    protected String getPropertyHeader(String propertyName) {
        String header = propertyToHeader.get(propertyName);
        if (header == null) {
            header = DefaultFieldFactory.createCaptionByPropertyId(propertyName);
        }
        return header;
    }

    @Override
    protected Component initContent() {
        return getStrategy().getLayout();
    }

    public InlineEditableCollection<ET> setUIStrategy(UIStrategy s) {
        uiStrategy = s;
        return this;
    }

    private Strategy getStrategy() {
        if (strategy == null) {
            switch (uiStrategy) {
                case TABLE:
                    strategy = new TableStrategyImpl();
                    break;
                case DEFAULT:
                    strategy = new GridStrategyImpl();
            }
            strategy.setVisibleProperties(visibleProperties);
        }
        return strategy;
    }

    @Override
    protected void setInternalValue(Collection newValue) {
        super.setInternalValue(newValue);
        getStrategy().clear();
        Collection<ET> value = newValue;
        if (value != null) {
            for (ET v : value) {
                getStrategy().addElement(v);
            }
        }
        getStrategy().onElementAdded();

    }

    @Override
    public Class<? extends Collection> getType() {
        return Collection.class;
    }

    /**
     * Design interface and provide different strategies (Table, Grid,
     * CssLayout)
     */
    private interface Strategy<T> {

        public void addElement(T v);

        public void setPersisted(T v, boolean persisted);

        public void removeElement(T v);

        public Layout getLayout();

        public void setVisibleProperties(List<Object> visibleProperties);

        public void clear();

        public void onElementAdded();

    }

    class GridStrategyImpl extends GridLayout implements
            InlineEditableCollection.Strategy<ET> {

        List<ET> items = new ArrayList<ET>();

        List<Object> visibleProperties;

        boolean inited = false;

        public GridStrategyImpl() {
            setSpacing(true);
        }

        @Override
        public void setVisibleProperties(List<Object> visibleProperties) {
            this.visibleProperties = visibleProperties;
        }

        private List<Object> getVisibleProperties() {
            if (visibleProperties == null) {

                visibleProperties = new ArrayList<Object>();

                for (java.lang.reflect.Field f : editorType.getDeclaredFields()) {
                    // field order can be counted since jdk6 
                    if (Field.class.isAssignableFrom(f.getType())) {
                        visibleProperties.add(f.getName());
                    }
                }

            }
            return visibleProperties;
        }

        @Override
        public void addElement(final ET v) {
            ensureInited();
            items.add(v);
            MBeanFieldGroup<ET> fg = getFieldGroupFor(v);
            for (Object property : getVisibleProperties()) {
                Component c = fg.getField(property);
                addComponent(c);
                setComponentAlignment(c, Alignment.MIDDLE_LEFT);
            }
            addComponent(new MButton(FontAwesome.TRASH_O).withListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            removeElement(v);
                        }
                    }).withStyleName(ValoTheme.BUTTON_ICON_ONLY));
        }

        @Override
        public void removeElement(ET v) {
            int index = items.indexOf(v);
            items.remove(index);
            int row = index + 1;
            removeRow(row);
        }

        @Override
        public Layout getLayout() {
            return this;
        }

        @Override
        public void setPersisted(ET v, boolean persisted) {
            int row = items.indexOf(v) + 1;
            Button c = (Button) getComponent(getVisibleProperties().size(),
                    row);
            if (persisted) {
                c.setDescription(getDeleteElementDescription());
            } else {
                for (int i = 0; i < getVisibleProperties().size(); i++) {
                    try {
                        AbstractField f = (AbstractField) (Field) getComponent(i,
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
                setColumns(getVisibleProperties().size() + 1);
                for (Object property : getVisibleProperties()) {
                    Label header = new Label(getPropertyHeader(property.
                            toString()));
                    header.setWidthUndefined();
                    addComponent(header);
                }
                newLine();
                inited = true;
            }
        }

        @Override
        public void clear() {
            if (inited) {
                items.clear();
                int rows = inited ? 1 : 0;
                while (getRows() > rows) {
                    removeRow(rows);
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
            if (allowNewItems) {
                newInstance = createInstance();
                addElement(newInstance);
                setPersisted(newInstance, false);
            }
        }

    }

    class TableStrategyImpl extends MVerticalLayout implements
            InlineEditableCollection.Strategy<ET> {

        MTable table;

        MButton addButton = new MButton(FontAwesome.PLUS,
                new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        addElement(createInstance());
                    }
                });

        List<Object> visibleProperties;

        IdentityHashMap<ET, MButton> elementToDelButton = new IdentityHashMap<ET, MButton>();

        boolean inited = false;

        public TableStrategyImpl() {
            setMargin(false);
            setHeight("300px");
        }

        @Override
        public void attach() {
            super.attach();
            ensureInited();
        }

        @Override
        public void setVisibleProperties(List<Object> visibleProperties) {
            this.visibleProperties = visibleProperties;
        }

        private List<Object> getVisibleProperties() {
            if (visibleProperties == null) {

                visibleProperties = new ArrayList<Object>();

                for (java.lang.reflect.Field f : editorType.getDeclaredFields()) {
                    // field order can be counted since jdk6 
                    if (Field.class.isAssignableFrom(f.getType())) {
                        visibleProperties.add(f.getName());
                    }
                }

            }
            return visibleProperties;
        }

        @Override
        public void addElement(final ET v) {
            ensureInited();
            table.addBeans(v);
        }

        @Override
        public void removeElement(ET v) {
            table.removeItem(v);
        }

        @Override
        public Layout getLayout() {
            return this;
        }

        @Override
        public void setPersisted(ET v, boolean persisted) {
            // NOP
        }

        private void ensureInited() {
            if (!inited) {
                table = new MTable(elementType);
                for (Object propertyId : getVisibleProperties()) {
                    table.addGeneratedColumn(propertyId,
                            new Table.ColumnGenerator() {

                                @Override
                                public Object generateCell(Table source,
                                        Object itemId,
                                        Object columnId) {
                                    MBeanFieldGroup<ET> fg = getFieldGroupFor(
                                            (ET) itemId);
                                    return fg.getField(columnId);
                                }
                            });

                }
                table.addGeneratedColumn("__ACTIONS",
                        new Table.ColumnGenerator() {

                            @Override
                            public Object generateCell(Table source,
                                    final Object itemId,
                                    Object columnId) {

                                MButton b = new MButton(FontAwesome.TRASH_O).
                                withListener(
                                        new Button.ClickListener() {
                                            @Override
                                            public void buttonClick(
                                                    Button.ClickEvent event) {
                                                        removeElement(
                                                                (ET) itemId);
                                                    }
                                        }).withStyleName(
                                        ValoTheme.BUTTON_ICON_ONLY);
                                b.setDescription(getDeleteElementDescription());
                                elementToDelButton.put((ET) itemId, b);
                                return b;
                            }
                        });
                table.setColumnHeader("__ACTIONS", "");
                ArrayList<Object> cols = new ArrayList<Object>(
                        getVisibleProperties());
                cols.add("__ACTIONS");
                table.setVisibleColumns(cols.toArray());
                addComponent(table);
                expand(table);
                if (allowNewItems) {
                    addComponent(addButton);
                }
                inited = true;
            }
        }

        @Override
        public void clear() {
            if (inited) {
                table.removeAllItems();
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
            // NOP
        }

    }
}
