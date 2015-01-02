package org.vaadin.maddon.fields;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.vaadin.maddon.BeanBinder;
import org.vaadin.maddon.MBeanFieldGroup;
import org.vaadin.maddon.MBeanFieldGroup.FieldGroupListener;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.form.AbstractForm;

/**
 * A field suitable for editing collection of referenced objects tied to parent
 * object only. E.g. OneToMany/ElementCollection fields in JPA world.
 * <p>
 * Some features/restrictions: The field is valid when all elements are valid.
 * The field is always non buffered The element type needs to have an empty
 * paremeter constructor or user must provide an Instantiator.
 *
 * <p>
 * By default the field always contains an empty instance to create new rows.
 * This can be configured to contain a separate add button or disable adding
 * altogether.
 *
 * @author Matti Tahvonen
 * @param <ET> The type in the entity collection. The type must have empty
 * paremeter constructor or you have to provide Instantiator.
 */
public class InlineEditableCollection<ET> extends CustomField<Collection> {

    private Instantiator<ET> instantiator;
    private Instantiator<?> editorInstantiator;

    private Class<ET> elementType;

    private Class<?> editorType;
    private Strategy strategy;
    private ET newInstance;

    FieldGroupListener fieldGroupListener = new FieldGroupListener() {

        @Override
        public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
            if (beanFieldGroup.getItemDataSource().getBean() == newInstance) {
                addNextNewElement();
            }
        }
    };

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
        newInstance = createInstance();
        strategy.addPojo(newInstance);
    }

    public interface Instantiator<ET> {

        ET create();
    }

    public InlineEditableCollection(Class<ET> elementType,
            Class formType) {
        this.elementType = elementType;
        this.editorType = formType;
    }

    public InlineEditableCollection(Class<ET> elementType, Instantiator i,
            Class<AbstractForm<ET>> formType) {
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
    }

    @Override
    protected Component initContent() {
        strategy = new GridStrategyImpl();
        Collection<ET> value = getValue();
        if (value != null) {
            for (ET v : value) {
                strategy.addPojo(v);
            }
        }
        addNextNewElement();

        return strategy.getLayout();
    }
    public static final String _ACTIONS_COLUMN_ID = "_actions";

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

        public void removePojo(T v);

        public Layout getLayout();

    }

    class GridStrategyImpl extends GridLayout implements
            InlineEditableCollection.Strategy<ET> {

        List<ET> items = new ArrayList<ET>();
        
        List<Object> visibleProperties;

        public GridStrategyImpl() {
            setSpacing(true);
        }
        
        private List<Object> getVisibleProperties() {
            if(visibleProperties == null) {
                // create temp instance and get bound properties
                ET newInstance = createInstance();
                FieldGroup fg = getFieldGroupFor(newInstance);
                visibleProperties = new ArrayList(fg.getBoundPropertyIds());
                pojoToEditor.remove(newInstance);
            }
            return visibleProperties;
        }

        @Override
        public void addPojo(final ET v) {
            setColumns(getVisibleProperties().size() + 1);
            items.add(v);
            MBeanFieldGroup<ET> fg = getFieldGroupFor(v);
            for (Object property : getVisibleProperties()) {
                addComponent(fg.getField(property));
            }
            addComponent(new MButton(FontAwesome.TRASH_O).withListener(
                    new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            removeElement(v);
                        }
                    }).withStyleName(ValoTheme.BUTTON_ICON_ONLY));
            this.newLine();
        }

        @Override
        public void removePojo(ET v) {
            int index = items.indexOf(v);
            items.remove(index);
            int row = index;
            removeRow(row);
        }

        @Override
        public Layout getLayout() {
            return this;
        }

    }

}
