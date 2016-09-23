package org.vaadin.viritin.fields;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.util.ReflectTools;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.MBeanFieldGroup.FieldGroupListener;

/**
 * A field to edit simple map structures like string to Integer/Double/Float 
 * maps. The field is still EXPERIMENTAL, so the should be considered less
 * stable than other Viritin API.
 * 
 * @author Matti Tahvonen
 * @param <K> The type of key in the edited map
 * @param <V> The type of value in the edited map
 */
public class MapField<K, V> extends CustomField<Map> {

    private static final Method addedMethod;
    private static final Method removedMethod;

    static {
        addedMethod = ReflectTools.findMethod(ElementAddedListener.class,
                "elementAdded", ElementAddedEvent.class);
        removedMethod = ReflectTools.findMethod(ElementRemovedListener.class,
                "elementRemoved", ElementRemovedEvent.class);
    }

    private GridLayout mainLayout = new GridLayout(3, 1);

    private Class<K> keyType;
    private Class<V> valueType;

    private Class<?> editorType;
    protected K newInstance;

    private final FieldGroupListener fieldGroupListener = new FieldGroupListener() {

        @Override
        public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
            if (beanFieldGroup.getItemDataSource().getBean() == newInstance) {
                if (!getFieldGroupFor(newInstance).valueEditor.isValid()) {
                    return;
                }
                getAndEnsureValue().put(newInstance, null);
                fireEvent(new ElementAddedEvent(MapField.this,
                        newInstance));
                setPersisted(newInstance, true);
                onElementAdded();
            }
            // TODO could optimize for only repainting on changed validity
            fireValueChange(false);
        }
    };
    private boolean allowNewItems = true;
    private boolean allowRemovingItems = true;
    private boolean allowEditItems = true;
    private final Map<K, EntryEditor> pojoToEditor = new HashMap<>();
    private EntryEditor newEntryEditor;

    public MapField() {
    }

    public MapField(Class<K> elementType,
            Class<?> formType) {
        this.keyType = elementType;
        this.editorType = formType;
    }

    private void ensureInited() {
        if (mainLayout.getComponentCount() == 0) {
            mainLayout.addComponent(new Label("Key ->"));
            mainLayout.addComponent(new Label("Value"));
            mainLayout.addComponent(new Label("Delete entry"));
        }
    }

    @Override
    public Class<? extends Map> getType() {
        return Map.class;
    }

    public MapField<K, V> addElementAddedListener(
            ElementAddedListener<K> listener) {
        addListener(ElementAddedEvent.class, listener, addedMethod);
        return this;
    }

    public MapField<K, V> removeElementAddedListener(
            ElementAddedListener listener) {
        removeListener(ElementAddedEvent.class, listener, addedMethod);
        return this;
    }

    public MapField<K, V> addElementRemovedListener(
            ElementRemovedListener<K> listener) {
        addListener(ElementRemovedEvent.class, listener, removedMethod);
        return this;
    }

    public MapField<K, V> removeElementRemovedListener(
            ElementRemovedListener listener) {
        removeListener(ElementRemovedEvent.class, listener, removedMethod);
        return this;
    }

    public boolean isAllowNewItems() {
        return allowNewItems;
    }

    public boolean isAllowRemovingItems() {
        return allowRemovingItems;
    }

    public boolean isAllowEditItems() {
        return allowEditItems;
    }

    public MapField<K, V> setAllowEditItems(boolean allowEditItems) {
        this.allowEditItems = allowEditItems;
        return this;
    }

    public MapField<K, V> setAllowRemovingItems(boolean allowRemovingItems) {
        this.allowRemovingItems = allowRemovingItems;
        return this;
    }

    public MapField<K, V> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        super.validate();
        Map<K, V> v = getValue();
        if (v != null) {
            for (K o : v.keySet()) {
                // TODO, should validate both key and value
//                for (Field f : getFieldGroupFor((K) o).getFields()) {
//                    f.validate();
//                }
            }
        }
    }

    private Map<K, V> getAndEnsureValue() {
        Map<K, V> value = getValue();
        if (value == null) {
            if (getPropertyDataSource() == null) {
                // this should never happen :-)
                return new HashMap();
            }
            Class fieldType = getPropertyDataSource().getType();
            if (fieldType.isInterface()) {
                value = new HashMap<>();
            } else {
                try {
                    value = (Map) fieldType.newInstance();
                } catch (Exception ex) {
                    throw new RuntimeException(
                            "Could not instantiate the used colleciton type", ex);
                }
            }
            getPropertyDataSource().setValue(value);
        }
        return value;
    }

    public MapField<K, V> setAllowNewElements(
            boolean allowNewItems) {
        this.allowNewItems = allowNewItems;
        return this;
    }

    public Class<K> getKeyType() {
        return keyType;
    }

    protected TextField createKeyEditorInstance() {
        MTextField tf = new MTextField().withInputPrompt("key");
        return tf;
    }

    protected TextField createValueEditorInstance() {
        MTextField tf = new MTextField().withInputPrompt("value");
        return tf;
    }

    private void replaceValue(K key, String value) {
        if (key == null) {
            // new value without proper key, ignore
            return;
        }
        K tKey;
        try {
            tKey = (K) key;
        } catch (ClassCastException e) {
            try {
                tKey = keyType.getConstructor(String.class).newInstance(key);
            } catch (Exception ex) {
                throw new RuntimeException("No suitable constructor found", ex);
            }
        }
        V tVal;
        try {
            tVal = (V) value;
        } catch (ClassCastException e) {
            try {
                tVal = valueType.getConstructor(String.class).newInstance(value);
            } catch (Exception ex) {
                throw new RuntimeException("No suitable constructor found", ex);
            }
        }

        getAndEnsureValue().put(tKey, tVal);

    }

    private void renameValue(Object oldKey, String key) {
        K tKey;
        try {
            tKey = (K) key;
        } catch (ClassCastException e) {
            try {
                tKey = keyType.getConstructor(String.class).newInstance(key);
            } catch (Exception ex) {
                throw new RuntimeException("No suitable constructor found", ex);
            }
        }
        EntryEditor e;
        V value;
        if (oldKey != null) {
            value = getAndEnsureValue().remove(oldKey);
            e = pojoToEditor.remove(oldKey);
        } else {
            e = newEntryEditor;

            String strValue = newEntryEditor.valueEditor.getValue();
            try {
                value = (V) strValue;
            } catch (ClassCastException ex) {
                try {
                    value = valueType.getConstructor(String.class).newInstance(strValue);
                } catch (Exception ex1) {
                    value = null;
                }
            }
            e.delete.setEnabled(true);
            // Old editor is used for the new value, create a new one
            createNewEntryRow();
        }
        e.oldKey = tKey;
        getAndEnsureValue().put(tKey, value);
        pojoToEditor.put(tKey, e);

    }

    protected final EntryEditor getFieldGroupFor(K key) {
        EntryEditor ee = pojoToEditor.get(key);
        if (ee == null) {
            final TextField k = createKeyEditorInstance();

            final TextField v = createValueEditorInstance();

            // TODO fieldgroup listener
            final EntryEditor ee1 = ee = new EntryEditor(k, v, key);

            // TODO listen for all changes for proper modified/validity changes
            pojoToEditor.put(key, ee);
        }
        return ee;
    }

    public void addElement(K key, V value) {
        getAndEnsureValue().put(key, value);
        addInternalElement(key, value);
        fireValueChange(false);
        fireEvent(new ElementAddedEvent<>(this, key));
    }

    public void removeElement(K keyToBeRemoved) {
        removeInternalElement(keyToBeRemoved);
        getAndEnsureValue().remove(keyToBeRemoved);
        fireValueChange(false);
        fireEvent(new ElementRemovedEvent<>(this, keyToBeRemoved));
    }

    @Override
    protected Component initContent() {
        return getLayout();
    }

    @Override
    protected void setInternalValue(Map newValue) {
        super.setInternalValue(newValue);
        clearCurrentEditors();
        Map<K, V> value = newValue;
        if (value != null) {

            for (Map.Entry<K, V> entry : value.entrySet()) {
                K key = entry.getKey();
                V value1 = entry.getValue();
                addInternalElement(key, value1);
            }
        }
        if (isAllowNewItems()) {
            createNewEntryRow();
        }
        onElementAdded();

    }

    private void createNewEntryRow() throws ReadOnlyException {
        TextField keyEditor = createKeyEditorInstance();
        TextField valueEditor = createKeyEditorInstance();
        newEntryEditor = new EntryEditor(keyEditor, valueEditor, null);

        addRowForEntry(newEntryEditor, null, null);
    }

    protected void addInternalElement(K k, V v) {
        ensureInited();
        EntryEditor fieldGroupFor = getFieldGroupFor(k);
        addRowForEntry(fieldGroupFor, k, v);

    }

    private void addRowForEntry(EntryEditor editor, K k, V v) throws ReadOnlyException {
        editor.bindValues(k, v);
        getLayout().addComponents(editor.keyEditor, editor.valueEditor, editor.delete);
    }

    protected void setPersisted(K v, boolean persisted) {

        // TODO create new "draft row"
    }

    protected void removeInternalElement(K v) {
        // TODO remove from layout
    }

    protected Layout getLayout() {
        return mainLayout;
    }

    protected void onElementAdded() {
        System.out.println("What now!?");
    }

    private void clearCurrentEditors() {
        while (mainLayout.getRows() > 1) {
            mainLayout.removeRow(1);
        }
    }

    public static class ElementAddedEvent<K> extends Component.Event {

        private final K key;

        public ElementAddedEvent(MapField source, K element) {
            super(source);
            this.key = element;
        }

        public K getKey() {
            return key;
        }

    }

    public static class ElementRemovedEvent<K> extends Component.Event {

        private final K key;

        public ElementRemovedEvent(MapField source, K element) {
            super(source);
            this.key = element;
        }

        public K getKey() {
            return key;
        }

    }

    public interface ElementAddedListener<K> extends Serializable {

        void elementAdded(ElementAddedEvent<K> e);
    }

    public interface ElementRemovedListener<K> extends Serializable {

        void elementRemoved(ElementRemovedEvent<K> e);
    }

    public interface Instantiator<ET> extends Serializable {

        ET create();
    }

    private class EntryEditor implements Serializable {

        TextField keyEditor;
        TextField valueEditor;
        Button delete;
        Object oldKey;

        public EntryEditor(TextField ke, TextField valueEditor, Object k) {
            this.keyEditor = ke;
            this.valueEditor = valueEditor;
            delete = new Button(FontAwesome.TRASH);
            delete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object removed = getValue().remove(oldKey);
                    pojoToEditor.remove(oldKey);
                    Iterator<Component> iterator = mainLayout.iterator();
                    int idx = 0;
                    while(iterator.next() != keyEditor) {
                        idx++;
                    }
                    mainLayout.removeRow((int) idx/3);
                }
            });

            this.oldKey = k;
            if (oldKey == null) {
                delete.setEnabled(false);
            }
        }

        private void bindValues(K k, V v) {
            keyEditor.setValue(k == null ? null : k.toString());
            valueEditor.setValue(v == null ? null : v.toString());
            keyEditor.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    renameValue(EntryEditor.this.oldKey, EntryEditor.this.keyEditor.getValue());
                }

            });
            valueEditor.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    replaceValue((K) EntryEditor.this.oldKey, EntryEditor.this.valueEditor.getValue());
                }

            });
        }

    }

}
