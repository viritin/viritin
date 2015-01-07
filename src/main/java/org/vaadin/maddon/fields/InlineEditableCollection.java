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
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.vaadin.maddon.BeanBinder;
import org.vaadin.maddon.MBeanFieldGroup;
import org.vaadin.maddon.MBeanFieldGroup.FieldGroupListener;
import org.vaadin.maddon.button.MButton;

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
                strategy.setPersisted(newInstance, true);
                addNextNewElement();
            }
            // TODO could optimize for only repainting on changed validity
            fireValueChange(false);
        }
    };
    private List<String> visibleProperties;
    private boolean allowNewItems;

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

    private void addNextNewElement() {
        if (allowNewItems) {
            newInstance = createInstance();
            strategy.addPojo(newInstance);
            strategy.setPersisted(newInstance, false);
        }
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
        strategy.addPojo(instance);
    }

    public void removeElement(ET elemnentToBeRemoved) {
        strategy.removePojo(elemnentToBeRemoved);
        getAndEnsureValue().remove(elemnentToBeRemoved);
        fireValueChange(false);
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

    public Strategy getStrategy() {
        if (strategy == null) {
            strategy = new GridStrategyImpl();
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
                getStrategy().addPojo(v);
            }
        }
        addNextNewElement();

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

        public void addPojo(T v);

        public void setPersisted(T v, boolean persisted);

        public void removePojo(T v);

        public Layout getLayout();

        public void setVisibleProperties(List<Object> visibleProperties);

        public void clear();

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
        public void addPojo(final ET v) {
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
        public void removePojo(ET v) {
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

    }
}
