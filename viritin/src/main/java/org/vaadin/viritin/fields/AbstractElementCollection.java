package org.vaadin.viritin.fields;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.StatusChangeEvent;
import com.vaadin.data.StatusChangeListener;
import com.vaadin.shared.util.SharedUtil;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.util.ReflectTools;

/**
 * NOTE, this V8 compatible version of this class should still be considered experimental.
 * <p>
 * A superclass for fields suitable for editing collection of referenced objects
 * tied to parent object only. E.g. OneToMany/ElementCollection fields in JPA
 * world.
 * <p>
 * Some features/restrictions:
 * <ul>
 * <li>The field is valid when all elements are valid.
 * <li>The field is always non buffered
 * <li>The element type needs to have an empty paremeter constructor or user
 * must provide an Instantiator.
 * </ul>
 *
 * @author Matti Tahvonen
 * @param <CT> The type of collection
 * @param <ET> The type in the entity collection. The type must have empty
 * paremeter constructor or you have to provide Instantiator.
 */
public abstract class AbstractElementCollection<ET, CT extends Collection<ET>> extends CustomField<CT> {

    private static final long serialVersionUID = 7785110162928180695L;
    private static ValidatorFactory factory;
    private transient Validator javaxBeanValidator;

    public static class ElementAddedEvent<ET> extends Component.Event {

        private static final long serialVersionUID = 2263765199849601501L;

        private final ET element;

        public ElementAddedEvent(AbstractElementCollection source, ET element) {
            super(source);
            this.element = element;
        }

        public ET getElement() {
            return element;
        }

    }

    public static class ElementRemovedEvent<ET> extends Component.Event {

        private static final long serialVersionUID = 574545902966053269L;

        private final ET element;

        public ElementRemovedEvent(AbstractElementCollection source, ET element) {
            super(source);
            this.element = element;
        }

        public ET getElement() {
            return element;
        }

    }

    public interface ElementAddedListener<ET> extends Serializable {

        void elementAdded(ElementAddedEvent<ET> e);
    }

    public interface ElementRemovedListener<ET> extends Serializable {

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

    public AbstractElementCollection<ET, CT> addElementAddedListener(
            ElementAddedListener<ET> listener) {
        addListener(ElementAddedEvent.class, listener, addedMethod);
        return this;
    }

    public AbstractElementCollection<ET, CT> removeElementAddedListener(
            ElementAddedListener listener) {
        removeListener(ElementAddedEvent.class, listener, addedMethod);
        return this;
    }

    public AbstractElementCollection<ET, CT> addElementRemovedListener(
            ElementRemovedListener<ET> listener) {
        addListener(ElementRemovedEvent.class, listener, removedMethod);
        return this;
    }

    public AbstractElementCollection<ET, CT> removeElementRemovedListener(
            ElementRemovedListener listener) {
        removeListener(ElementRemovedEvent.class, listener, removedMethod);
        return this;
    }

    private Instantiator<ET> instantiator;
    private Instantiator<?> oldEditorInstantiator;
    private EditorInstantiator<?, ET> newEditorInstantiator;

    private final Class<ET> elementType;

    private final Class<?> editorType;
    protected ET newInstance;

    private CT value;

    StatusChangeListener scl = new StatusChangeListener() {
        @Override
        public void statusChange(StatusChangeEvent event) {
            ET bean = (ET) event.getBinder().getBean();
            if (bean == newInstance) {
                // God dammit, you can't rely on BeanValidationBinder.isValid !
                // Returns true here even if some notnull fields are not set :-(
                // Thus, check also with own BeanValidation logi, this should 
                // also give a bare bones cross field validation support for
                // elements
                final Binder<?> binder = event.getBinder();
                final boolean valid = binder.isValid();
                if (!valid || !isValidBean(bean)) {
                    return;
                }
                getAndEnsureValue().add(newInstance);
                fireEvent(
                        new ElementAddedEvent<>(AbstractElementCollection.this,
                        newInstance));
                setPersisted(newInstance, true);
                onElementAdded();
            }
            // TODO
            fireValueChange();
        }

        private void fireValueChange() {
            // TODO FFS, old value with mutable object, eh
            // TODO FFS, how to detect 
            fireEvent(new ValueChangeEvent<>(AbstractElementCollection.this,
                    null, true));
        }
    };

    private List<String> visibleProperties;
    private boolean allowNewItems = true;
    private boolean allowRemovingItems = true;
    private boolean allowEditItems = true;

    public boolean isAllowNewItems() {
        return allowNewItems;
    }

    public boolean isAllowRemovingItems() {
        return allowRemovingItems;
    }

    public boolean isAllowEditItems() {
        return allowEditItems;
    }

    public AbstractElementCollection<ET, CT> setAllowEditItems(boolean allowEditItems) {
        this.allowEditItems = allowEditItems;
        return this;
    }

    public AbstractElementCollection<ET, CT> setAllowRemovingItems(boolean allowRemovingItems) {
        this.allowRemovingItems = allowRemovingItems;
        return this;
    }

    public AbstractElementCollection<ET, CT> withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    // TODO figure out if replacement for this is needed?
//    @Override
//    public void validate() throws Validator.InvalidValueException {
//        super.validate();
//        Collection v = getValue();
//        if(v != null) {
//            for (Object o : v) {
//                for (Field f : getFieldGroupFor((ET) o).getFields()) {
//                    f.validate();
//                }
//            }
//        }
//    }
    private Collection<ET> getAndEnsureValue() {
        CT value = getValue();
        if (value == null) {
            // TODO Can't be done anymore !? No?
            //Class fieldType = getPropertyDataSource().getType();
            Class fieldType = List.class;
            if (fieldType.isInterface()) {
                if (fieldType == List.class) {
                    value = (CT) new ArrayList<ET>();
                } else { // Set
                    value = (CT) new HashSet<ET>();
                }
            } else {
                try {
                    value = (CT) fieldType.newInstance();
                } catch (IllegalAccessException | InstantiationException ex) {
                    throw new RuntimeException(
                            "Could not instantiate the used colleciton type", ex);
                }
            }
            setValue(value);
        }
        return value;
    }

    /**
     * @param allowNewItems true if a new element row should be automatically
     * added
     * @return the configured field instance
     */
    public AbstractElementCollection<ET, CT> setAllowNewElements(
            boolean allowNewItems) {
        this.allowNewItems = allowNewItems;
        return this;
    }

    public interface Instantiator<ET> extends Serializable {

        ET create();
    }

    public interface EditorInstantiator<T, ET> extends Serializable {

        T create(ET entity);
    }

    public AbstractElementCollection(Class<ET> elementType,
            Class<?> formType) {
        this.elementType = elementType;
        this.editorType = formType;
    }

    public AbstractElementCollection(Class<ET> elementType, Instantiator<ET> i,
            Class<?> formType) {
        this.elementType = elementType;
        this.instantiator = i;
        this.editorType = formType;
    }

    public Class<ET> getElementType() {
        return elementType;
    }

    protected ET createInstance() {
        if (instantiator != null) {
            return instantiator.create();
        } else {
            try {
                return elementType.newInstance();
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    protected Object createEditorInstance(ET pojo) {
        if (newEditorInstantiator != null) {
            return newEditorInstantiator.create(pojo);
        } else {
            if (oldEditorInstantiator != null) {
                return oldEditorInstantiator.create();
            } else {
                try {
                    return editorType.newInstance();
                } catch (IllegalAccessException | InstantiationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public EditorInstantiator<?, ET> getNewEditorInstantiator() {
        return newEditorInstantiator;
    }

    public void setNewEditorInstantiator(
            EditorInstantiator<?, ET> editorInstantiator) {
        this.newEditorInstantiator = editorInstantiator;
    }

    public Instantiator<?> getEditorInstantiator() {
        return oldEditorInstantiator;
    }

    public void setEditorInstantiator(
            Instantiator<?> editorInstantiator) {
        this.oldEditorInstantiator = editorInstantiator;
    }

    private class EditorStuff implements Serializable {

        private static final long serialVersionUID = 5132645136059482705L;
        Binder<ET> bfg;
        Object editor;

        private EditorStuff(Binder<ET> editor, Object o) {
            this.bfg = editor;
            this.editor = o;
        }
    }

    private final Map<ET, EditorStuff> pojoToEditor = new IdentityHashMap<>();

    protected Binder<ET> instantiateBinder(Class<ET> elementClass) {
		return new BeanValidationBinder<>(elementClass);
	}
    
    protected Binder<ET> getFieldGroupFor(ET pojo) {
        EditorStuff es = pojoToEditor.get(pojo);
        if (es == null) {
            Object o = createEditorInstance(pojo);
            Binder<ET> binder = instantiateBinder(elementType);
            binder.bindInstanceFields(o);
            binder.setBean(pojo);
            binder.addStatusChangeListener(scl);
            es = new EditorStuff(binder, o);
            // TODO listen for all changes for proper modified/validity changes
            pojoToEditor.put(pojo, es);
        }
        return es.bfg;
    }

    protected Component getComponentFor(ET pojo, String property) {
        EditorStuff editorsstuff = pojoToEditor.get(pojo);
        if (editorsstuff == null) {
            Object o = createEditorInstance(pojo);
            Binder<ET> binder = instantiateBinder(elementType);
            binder.bindInstanceFields(o);
            binder.setBean(pojo);
            binder.addStatusChangeListener(scl);
            editorsstuff = new EditorStuff(binder, o);
            // TODO listen for all changes for proper modified/validity changes
            pojoToEditor.put(pojo, editorsstuff);
        }
        Component c = null;
        Optional<Binder.Binding<ET, ?>> binding = editorsstuff.bfg.getBinding(property);
        if (binding.isPresent()) {
            c = (Component) binding.get().getField();
        } else {
            try {
                // property that is not a property editor field but custom UI "column"
                java.lang.reflect.Field f = editorType.getDeclaredField(property);
                f.setAccessible(true);
                c = (Component) f.get(editorsstuff.editor);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(AbstractElementCollection.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
            if (c == null) {
                c = new Label("");
            }
        }

        return c;
    }

    public void addElement(ET instance) {
        getAndEnsureValue().add(instance);
        addInternalElement(instance);
        fireEvent(new ValueChangeEvent<>(this, null, true));
        fireEvent(new ElementAddedEvent<>(this, instance));
    }

    public void removeElement(ET elemnentToBeRemoved) {
        removeInternalElement(elemnentToBeRemoved);
        getAndEnsureValue().remove(elemnentToBeRemoved);
        fireEvent(new ValueChangeEvent<>(this, null, true));
        fireEvent(new ElementRemovedEvent<>(this, elemnentToBeRemoved));
    }

    public AbstractElementCollection<ET, CT> setVisibleProperties(
            List<String> properties) {
        visibleProperties = properties;
        return this;
    }

    public List<String> getVisibleProperties() {
        if (visibleProperties == null) {

            visibleProperties = new ArrayList<>();

            for (java.lang.reflect.Field f : editorType.getDeclaredFields()) {
                // field order can be counted since jdk6 
                if (HasValue.class.isAssignableFrom(f.getType())) {
                    visibleProperties.add(f.getName());
                }
            }

        }
        return visibleProperties;
    }

    public AbstractElementCollection<ET, CT> setVisibleProperties(
            List<String> properties, List<String> propertyHeaders) {
        visibleProperties = properties;
        Iterator<String> it = propertyHeaders.iterator();
        for (String prop : visibleProperties) {
            setPropertyHeader(prop, it.next());
        }
        return this;
    }

    private final Map<String, String> propertyToHeader = new HashMap<>();

    public AbstractElementCollection<ET, CT> setPropertyHeader(String propertyName,
            String propertyHeader) {
        propertyToHeader.put(propertyName, propertyHeader);
        return this;
    }

    protected String getPropertyHeader(String propertyName) {
        String header = propertyToHeader.get(propertyName);
        if (header == null) {
            header = SharedUtil.propertyIdToHumanFriendly(propertyName);
        }
        return header;
    }

    @Override
    protected Component initContent() {
        return getLayout();
    }

    @Override
    protected void doSetValue(CT newValue) {
        clear();
        value = newValue;
        if (value != null) {
            for (Object v : value) {
                addInternalElement((ET) v);
            }
        }
        onElementAdded();
    }

    abstract protected void addInternalElement(ET v);

    abstract protected void setPersisted(ET v, boolean persisted);

    abstract protected void removeInternalElement(ET v);

    abstract protected Layout getLayout();

    abstract protected void onElementAdded();

    @Override
    public CT getValue() {
        return value;
    }

    public boolean isValidBean(Object bean) {
        Set<ConstraintViolation<Object>> violations = getJavaxBeanValidator().validate(bean);
        return violations.isEmpty();
    }

    /**
     * Returns the underlying JSR-303 bean validator factory used. A factory is
     * created using {@link Validation} if necessary.
     *
     * @return {@link ValidatorFactory} to use
     */
    protected static ValidatorFactory getJavaxBeanValidatorFactory() {
        if (factory == null) {
            factory = Validation.buildDefaultValidatorFactory();
        }

        return factory;
    }

    /**
     * Returns a shared Validator instance to use. An instance is created using
     * the validator factory if necessary and thereafter reused.
     *
     * @return the JSR-303 {@link javax.validation.Validator} to use
     */
    protected Validator getJavaxBeanValidator() {
        if (javaxBeanValidator == null) {
            javaxBeanValidator = getJavaxBeanValidatorFactory().getValidator();
        }

        return javaxBeanValidator;
    }

}
