package org.vaadin.viritin.form;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.viritin.BeanBinder;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.MBeanFieldGroup.FieldGroupListener;
import org.vaadin.viritin.button.DeleteButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.PrimaryButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * Abstract super class for simple editor forms.
 *
 * @param <T> the type of the bean edited
 */
public abstract class AbstractForm<T> extends CustomComponent implements
        FieldGroupListener {

    public AbstractForm() {
        addAttachListener(new AttachListener() {
            @Override
            public void attach(AttachEvent event) {
                lazyInit();
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

    @Override
    public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
        adjustSaveButtonState();
        adjustResetButtonState();
    }

    protected void adjustSaveButtonState() {
        if (isAttached() && isEagerValidation() && isBound()) {
            boolean beanModified = fieldGroup.isBeanModified();
            boolean valid = fieldGroup.isValid();
            getSaveButton().setEnabled(beanModified && valid);
        }
    }

    protected boolean isBound() {
        return fieldGroup != null;
    }

    protected void adjustResetButtonState() {
        if (isAttached() && isEagerValidation() && isBound()) {
            boolean beanModified = fieldGroup.isBeanModified();
            getResetButton().setEnabled(beanModified);
        }
    }

    public interface SavedHandler<T> {

        void onSave(T entity);
    }

    public interface ResetHandler<T> {

        void onReset(T entity);
    }

    public interface DeleteHandler<T> {

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
            fieldGroup = BeanBinder.bind(entity, this);
            if (isEagerValidation()) {
                fieldGroup.withEagerValidation(this);
                adjustSaveButtonState();
                adjustResetButtonState();
            }
            setVisible(true);
            return fieldGroup;
        } else {
            setVisible(false);
            return null;
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
        Window window = new Window("Edit entry", this);
        window.setModal(true);
        UI.getCurrent().addWindow(window);
        focusFirst();
        return window;
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
    }

    protected void reset(Button.ClickEvent e) {
        resetHandler.onReset(getEntity());
    }

    protected void delete(Button.ClickEvent e) {
        deleteHandler.onDelete(getEntity());
    }

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

}
