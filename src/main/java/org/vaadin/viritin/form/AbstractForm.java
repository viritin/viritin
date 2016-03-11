package org.vaadin.viritin.form;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.util.ReflectTools;
import java.io.Serializable;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.MBeanFieldGroup.FieldGroupListener;
import org.vaadin.viritin.button.DeleteButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.PrimaryButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.groups.Default;
import org.vaadin.viritin.label.RichText;

/**
 * Abstract super class for simple editor forms.
 *
 * @param <T> the type of the bean edited
 */
public abstract class AbstractForm<T> extends CustomComponent implements
        FieldGroupListener {

    public static class ValidityChangedEvent<T> extends Component.Event {

        private static final Method method = ReflectTools.findMethod(
                ValidityChangedListener.class, "onValidityChanged",
                ValidityChangedEvent.class);

        public ValidityChangedEvent(Component source) {
            super(source);
        }

        @Override
        public AbstractForm<T> getComponent() {
            return (AbstractForm) super.getComponent();
        }

    }

    public interface ValidityChangedListener<T> extends Serializable {

        public void onValidityChanged(ValidityChangedEvent<T> event);
    }

    private Window popup;

    public AbstractForm() {
        addAttachListener(new AttachListener() {
            @Override
            public void attach(AttachEvent event) {
                lazyInit();
                adjustResetButtonState();
            }
        });
    }

    protected void lazyInit() {
        if (getCompositionRoot() == null) {
            setCompositionRoot(createContent());
            adjustSaveButtonState();
            adjustResetButtonState();
        }
    }

    private MBeanFieldGroup<T> fieldGroup;

    /**
     * The validity checked and cached on last change. Should be pretty much
     * always up to date due to eager changes. At least after onFieldGroupChange
     * call.
     */
    boolean isValid = false;

    private RichText beanLevelViolations;

    @Override
    public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
        boolean wasValid = isValid;
        isValid = fieldGroup.isValid();
        adjustSaveButtonState();
        adjustResetButtonState();
        if (wasValid != isValid) {
            fireValidityChangedEvent();
        }
        updateConstraintViolationsDisplay();
    }

    protected void updateConstraintViolationsDisplay() {
        if (beanLevelViolations != null) {
            Collection<String> errorMessages = getFieldGroup().
                    getBeanLevelValidationErrors();
            if (!errorMessages.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String e : errorMessages) {
                    sb.append(e);
                    sb.append("<br/>");
                }
                beanLevelViolations.setValue(sb.toString());
                beanLevelViolations.setVisible(true);
            } else {
                beanLevelViolations.setVisible(false);
                beanLevelViolations.setValue("");
            }
        }
    }

    public Component getConstraintViolationsDisplay() {
        if (beanLevelViolations == null) {
            beanLevelViolations = new RichText();
            beanLevelViolations.setVisible(false);
            beanLevelViolations.setStyleName(ValoTheme.LABEL_FAILURE);
        }
        return beanLevelViolations;
    }

    public boolean isValid() {
        return isValid;
    }

    protected void adjustSaveButtonState() {
        if (isEagerValidation() && isBound()) {
            boolean beanModified = fieldGroup.isBeanModified();
            getSaveButton().setEnabled(beanModified && isValid());
        }
    }

    protected boolean isBound() {
        return fieldGroup != null;
    }

    protected void adjustResetButtonState() {
        if (popup != null && popup.getParent() != null) {
            // Assume cancel button in a form opened to a popup also closes
            // it, allows closing via cancel button by default
            getResetButton().setEnabled(true);
            return;
        }
        if (isEagerValidation() && isBound()) {
            boolean modified = fieldGroup.isBeanModified();
            getResetButton().setEnabled(modified || popup != null);
        }
    }

    public void addValidityChangedListener(ValidityChangedListener<T> listener) {
        addListener(ValidityChangedEvent.class, listener,
                ValidityChangedEvent.method);
    }

    public void removeValidityChangedListener(
            ValidityChangedListener<T> listener) {
        removeListener(ValidityChangedEvent.class, listener,
                ValidityChangedEvent.method);
    }

    private void fireValidityChangedEvent() {
        fireEvent(new ValidityChangedEvent(this));
    }

    public interface SavedHandler<T> extends Serializable {

        void onSave(T entity);
    }

    public interface ResetHandler<T> extends Serializable {

        void onReset(T entity);
    }

    public interface DeleteHandler<T> extends Serializable {

        void onDelete(T entity);
    }

    private T entity;
    private SavedHandler<T> savedHandler;
    private ResetHandler<T> resetHandler;
    private DeleteHandler<T> deleteHandler;
    private boolean eagerValidation = true;

    public boolean isEagerValidation() {
        return eagerValidation;
    }

    /**
     * In case one is working with "detached entities" enabling eager validation
     * will highly improve usability. The validity of the form will be updated
     * on each changes and save/cancel buttons will reflect to the validity and
     * possible changes.
     *
     * @param eagerValidation true if the form should have eager validation
     */
    public void setEagerValidation(boolean eagerValidation) {
        this.eagerValidation = eagerValidation;
    }

    public MBeanFieldGroup<T> setEntity(T entity) {
        lazyInit();
        this.entity = entity;
        if (entity != null) {
            if (isBound()) {
                fieldGroup.unbind();
            }
            fieldGroup = bindEntity(entity);
            try {
                fieldGroup.setValidationGroups(getValidationGroups());
            } catch (Throwable e) {
                // Probably no Validation API available
            }

            for (Map.Entry<MBeanFieldGroup.MValidator<T>, Collection<AbstractComponent>> e : mValidators.
                    entrySet()) {
                fieldGroup.addValidator(e.getKey(), e.getValue().toArray(
                        new AbstractComponent[e.getValue().size()]));
            }
            for (Map.Entry<Class, AbstractComponent> e : validatorToErrorTarget.
                    entrySet()) {
                fieldGroup.setValidationErrorTarget(e.getKey(), e.getValue());
            }

            isValid = fieldGroup.isValid();
            if (isEagerValidation()) {
                fieldGroup.withEagerValidation(this);
                adjustSaveButtonState();
                adjustResetButtonState();
            }
            fieldGroup.hideInitialEmpyFieldValidationErrors();
            setVisible(true);
            return fieldGroup;
        } else {
            setVisible(false);
            return null;
        }
    }

    /**
     * Creates a field group, configures the fields, binds the entity to those
     * fields
     *
     * @param entity The entity to bind
     * @return the fieldGroup created
     */
    protected MBeanFieldGroup<T> bindEntity(T entity) {
        return BeanBinder.bind(entity, this, getNestedProperties());
    }
    
    private String[] nestedProperties;

    public String[] getNestedProperties() {
        return nestedProperties;
    }

    public void setNestedProperties(String... nestedProperties) {
        this.nestedProperties = nestedProperties;
    }

    /**
     * Sets the given object to be a handler for saved,reset,deleted, based on
     * what it happens to implement.
     *
     * @param handler the handler to be set as saved/reset/delete handler
     */
    public void setHandler(Object handler) {
        if (handler != null) {
            if (handler instanceof SavedHandler) {
                setSavedHandler((SavedHandler<T>) handler);
            }
            if (handler instanceof ResetHandler) {
                setResetHandler((ResetHandler) handler);
            }
            if (handler instanceof DeleteHandler) {
                setDeleteHandler((DeleteHandler) handler);
            }
        }
    }

    public void setSavedHandler(SavedHandler<T> savedHandler) {
        this.savedHandler = savedHandler;
        getSaveButton().setVisible(this.savedHandler != null);
    }

    public void setResetHandler(ResetHandler<T> resetHandler) {
        this.resetHandler = resetHandler;
        getResetButton().setVisible(this.resetHandler != null);
    }

    public void setDeleteHandler(DeleteHandler<T> deleteHandler) {
        this.deleteHandler = deleteHandler;
        getDeleteButton().setVisible(this.deleteHandler != null);
    }

    public ResetHandler<T> getResetHandler() {
        return resetHandler;
    }

    public SavedHandler<T> getSavedHandler() {
        return savedHandler;
    }

    public DeleteHandler<T> getDeleteHandler() {
        return deleteHandler;
    }

    public Window openInModalPopup() {
        popup = new Window("Edit entry", this);
        popup.setModal(true);
        UI.getCurrent().addWindow(popup);
        focusFirst();
        return popup;
    }

    /**
     *
     * @return the last Popup into which the Form was opened with
     * #openInModalPopup method or null if the form hasn't been use in window
     */
    public Window getPopup() {
        return popup;
    }
    
    /**
     * If the form is opened into a popup window using openInModalPopup(), you
     * you can use this method to close the popup.
     */
    public void closePopup() {
        if(popup != null) {
            popup.close();
            popup = null;
        }
    }


    /**
     * @return A default toolbar containing save/cancel/delete buttons
     */
    public HorizontalLayout getToolbar() {
        return new MHorizontalLayout(
                getSaveButton(),
                getResetButton(),
                getDeleteButton()
        );
    }

    protected Button createCancelButton() {
        return new MButton("Cancel")
                .withVisible(false);
    }
    private Button resetButton;

    public Button getResetButton() {
        if (resetButton == null) {
            setResetButton(createCancelButton());
        }
        return resetButton;
    }

    public void setResetButton(Button resetButton) {
        this.resetButton = resetButton;
        this.resetButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                reset(event);
            }
        });
    }

    protected Button createSaveButton() {
        return new PrimaryButton("Save")
                .withVisible(false);
    }

    private Button saveButton;

    public void setSaveButton(Button saveButton) {
        this.saveButton = saveButton;
        saveButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                save(event);
            }
        });
    }

    public Button getSaveButton() {
        if (saveButton == null) {
            setSaveButton(createSaveButton());
        }
        return saveButton;
    }

    protected Button createDeleteButton() {
        return new DeleteButton("Delete")
                .withVisible(false);
    }

    private Button deleteButton;

    public void setDeleteButton(final Button deleteButton) {
        this.deleteButton = deleteButton;
        deleteButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                delete(event);
            }
        });
    }

    public Button getDeleteButton() {
        if (deleteButton == null) {
            setDeleteButton(createDeleteButton());
        }
        return deleteButton;
    }

    protected void save(Button.ClickEvent e) {
        savedHandler.onSave(getEntity());
        getFieldGroup().setBeanModified(false);
        adjustResetButtonState();
        adjustSaveButtonState();
    }

    protected void reset(Button.ClickEvent e) {
        resetHandler.onReset(getEntity());
        getFieldGroup().setBeanModified(false);
        adjustResetButtonState();
        adjustSaveButtonState();
    }

    protected void delete(Button.ClickEvent e) {
        deleteHandler.onDelete(getEntity());
    }

    /**
     * Focuses the first field found from the form. It often improves UX to 
     * call this method, or focus another field, when you assign a bean for 
     * editing.
     */
    public void focusFirst() {
        Component compositionRoot = getCompositionRoot();
        findFieldAndFocus(compositionRoot);
    }

    private boolean findFieldAndFocus(Component compositionRoot) {
        if (compositionRoot instanceof AbstractComponentContainer) {
            AbstractComponentContainer cc = (AbstractComponentContainer) compositionRoot;

            for (Component component : cc) {
                if (component instanceof AbstractTextField) {
                    AbstractTextField abstractTextField = (AbstractTextField) component;
                    abstractTextField.selectAll();
                    return true;
                }
                if (component instanceof AbstractField) {
                    AbstractField abstractField = (AbstractField) component;
                    abstractField.focus();
                    return true;
                }
                if (component instanceof AbstractComponentContainer) {
                    if (findFieldAndFocus(component)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method should return the actual content of the form, including
     * possible toolbar.
     *
     * Example implementation could look like this:      <code>
     * public class PersonForm extends AbstractForm&lt;Person&gt; {
     *
     *     private TextField firstName = new MTextField(&quot;First Name&quot;);
     *     private TextField lastName = new MTextField(&quot;Last Name&quot;);
     *
     *     \@Override
     *     protected Component createContent() {
     *         return new MVerticalLayout(
     *                 new FormLayout(
     *                         firstName,
     *                         lastName
     *                 ),
     *                 getToolbar()
     *         );
     *     }
     * }
     * </code>
     *
     * @return the content of the form
     *
     */
    protected abstract Component createContent();

    public MBeanFieldGroup<T> getFieldGroup() {
        return fieldGroup;
    }

    public T getEntity() {
        return entity;
    }

    private final LinkedHashMap<MBeanFieldGroup.MValidator<T>, Collection<AbstractComponent>> mValidators
            = new LinkedHashMap<MBeanFieldGroup.MValidator<T>, Collection<AbstractComponent>>();

    private final Map<Class, AbstractComponent> validatorToErrorTarget = new LinkedHashMap<Class, AbstractComponent>();

    private Class<?>[] validationGroups;

    /**
     * @return the JSR 303 bean validation groups that should be
     * used to validate the bean
     */
    public Class<?>[] getValidationGroups() {
        if (validationGroups == null) {
            return new Class<?>[]{Default.class};
        }
        return validationGroups;
    }

    /**
     * @param validationGroups the JSR 303 bean validation groups that should be
     * used to validate the bean. Note, that groups currently only affect 
     * cross-field/bean-level validation.
     */
    public void setValidationGroups(Class<?>... validationGroups) {
        this.validationGroups = validationGroups;
        if(getFieldGroup() != null) {
            getFieldGroup().setValidationGroups(validationGroups);
        }
    }

    public void setValidationErrorTarget(Class aClass,
            AbstractComponent errorTarget) {
        validatorToErrorTarget.put(aClass, errorTarget);
        if (getFieldGroup() != null) {
            getFieldGroup().setValidationErrorTarget(aClass, errorTarget);
        }
    }

    /**
     * EXPERIMENTAL: The cross field validation support is still experimental
     * and its API is likely to change.
     *
     * @param validator a validator that validates the whole bean making cross
     * field validation much simpler
     * @param fields the ui fields that this validator affects and on which a
     * possible error message is shown.
     * @return this FieldGroup
     */
    public AbstractForm<T> addValidator(
            MBeanFieldGroup.MValidator<T> validator,
            AbstractComponent... fields) {
        mValidators.put(validator, Arrays.asList(fields));
        if (getFieldGroup() != null) {
            getFieldGroup().addValidator(validator, fields);
        }
        return this;
    }

    public AbstractForm<T> removeValidator(
            MBeanFieldGroup.MValidator<T> validator) {
        Collection<AbstractComponent> remove = mValidators.remove(validator);
        if (remove != null) {
            if (getFieldGroup() != null) {
                getFieldGroup().removeValidator(validator);
            }
        }
        return this;
    }

    /**
     * Removes all MValidators added the MFieldGroup
     *
     * @return the instance
     */
    public AbstractForm<T> clearValidators() {
        mValidators.clear();
        if (getFieldGroup() != null) {
            getFieldGroup().clear();
        }
        return this;
    }

}
