/*
 * Copyright 2017 Matti Tahvonen.
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
package org.vaadin.viritin.form;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.Binder;
import com.vaadin.data.StatusChangeEvent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.io.Serializable;
import org.vaadin.viritin.button.DeleteButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.button.PrimaryButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 *
 * @author mstahv
 */
public abstract class AbstractForm<T> extends CustomComponent {

    private boolean settingBean;

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
    private String modalWindowTitle = "Edit entry";
    private String saveCaption = "Save";
    private String deleteCaption = "Delete";
    private String cancelCaption = "Cancel";
    private Window popup;
    private Binder<T> binder;
    private boolean hasChanges = false;

    public AbstractForm(Class<T> entityType) {
        addAttachListener(new AttachListener() {

            private static final long serialVersionUID = 3193438171004932112L;

            @Override
            public void attach(AttachEvent event) {
                lazyInit();
            }
        });
        binder = new BeanValidationBinder<>(entityType);
        binder.addValueChangeListener(e -> {
            // binder.hasChanges is not really usefull so track it manually
            if (!settingBean) {
                hasChanges = true;
            }
        });
        binder.addStatusChangeListener(e -> {
            // TODO optimize this
            // TODO see if explicitly calling writeBean would write also invalid
            // values -> would make functionality more logical and easier for 
            // users to do validation and error reporting

            // Eh, value change listener is called after status change listener, so
            // ensure flag is on...
            if (!settingBean) {
                hasChanges = true;
            }
            adjustResetButtonState();
            adjustSaveButtonState();
        });
    }

    /**
     * Sets the object to be edited by this form. This method binds all fields
     * from this form to given objects.
     * <p>
     * If your form needs to manually configure something based on the state of
     * the edited object, you can override this method to do that either before
     * the object is bound to fields or to do something after the bean binding.
     *
     * @param entity the object to be edited by this form
     */
    public void setEntity(T entity) {
        this.entity = entity;
        this.settingBean = true;
        lazyInit();
        if (entity != null) {
            binder.setBean(entity);
            hasChanges = false;
            setVisible(true);
        } else {
            binder.setBean(null);
            hasChanges = false;
            setVisible(false);
        }
        settingBean = false;
    }

    /**
     * @return true if bean has been changed since last setEntity call.
     */
    public boolean hasChanges() {
        return hasChanges;
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

    public String getSaveCaption() {
        return saveCaption;
    }

    public void setSaveCaption(String saveCaption) {
        this.saveCaption = saveCaption;
    }

    public String getModalWindowTitle() {
        return modalWindowTitle;
    }

    public void setModalWindowTitle(String modalWindowTitle) {
        this.modalWindowTitle = modalWindowTitle;
    }

    public String getDeleteCaption() {
        return deleteCaption;
    }

    public void setDeleteCaption(String deleteCaption) {
        this.deleteCaption = deleteCaption;
    }

    public String getCancelCaption() {
        return cancelCaption;
    }

    public void setCancelCaption(String cancelCaption) {
        this.cancelCaption = cancelCaption;
    }

    public Binder<T> getBinder() {
        return binder;
    }

    public void setBinder(Binder<T> binder) {
        this.binder = binder;
    }

    protected void lazyInit() {
        if (getCompositionRoot() == null) {
            setCompositionRoot(createContent());
            bind();
        }
    }

    /**
     * By default just does simple name based binding. Override this method to
     * customize the binding.
     */
    protected void bind() {
        binder.bindInstanceFields(this);
    }

    /**
     * This method should return the actual content of the form, including
     * possible toolbar.
     *
     * Use setEntity(T entity) to fill in the data. Am example implementation
     * could look like this:
     * <pre><code>
     * public class PersonForm extends AbstractForm&lt;Person&gt; {
     *
     *     private TextField firstName = new MTextField(&quot;First Name&quot;);
     *     private TextField lastName = new MTextField(&quot;Last Name&quot;);
     *
     *    {@literal @}Override
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
     * </code></pre>
     *
     * @return the content of the form
     *
     */
    protected abstract Component createContent();

    protected void adjustSaveButtonState() {
        if (isBound()) {
            boolean valid = binder.isValid();
            getSaveButton().setEnabled(hasChanges() && valid);
        }
    }

    public Button getSaveButton() {
        if (saveButton == null) {
            setSaveButton(createSaveButton());
        }
        return saveButton;
    }

    protected Button createSaveButton() {
        return new PrimaryButton(getSaveCaption())
                .withVisible(false);
    }

    private Button saveButton;

    protected boolean isBound() {
        return binder != null && binder.getBean() != null;
    }

    protected Button createResetButton() {
        return new MButton(getCancelCaption())
                .withVisible(false);
    }
    private Button resetButton;

    public Button getResetButton() {
        if (resetButton == null) {
            setResetButton(createResetButton());
        }
        return resetButton;
    }

    public void setResetButton(Button resetButton) {
        this.resetButton = resetButton;
        this.resetButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -19755976436277487L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                reset(event);
            }
        });
    }

    protected Button createDeleteButton() {
        return new DeleteButton(getDeleteCaption())
                .withVisible(false);
    }

    private Button deleteButton;

    public void setDeleteButton(final Button deleteButton) {
        this.deleteButton = deleteButton;
        deleteButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -2693734056915561664L;

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

    protected void adjustResetButtonState() {
        if (popup != null && popup.getParent() != null) {
            // Assume cancel button in a form opened to a popup also closes
            // it, allows closing via cancel button by default
            getResetButton().setEnabled(true);
            return;
        }
        if (isBound()) {
            boolean modified = hasChanges();
            getResetButton().setEnabled(modified || popup != null);
        }
    }

    public void setSaveButton(Button button) {
        this.saveButton = button;
        saveButton.addClickListener(new Button.ClickListener() {

            private static final long serialVersionUID = -2058398434893034442L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                save(event);
            }
        });
    }

    /**
     * @return the currently edited entity or null if the form is currently
     * unbound
     */
    public T getEntity() {
        return entity;
    }

    protected void save(Button.ClickEvent e) {
        savedHandler.onSave(getEntity());
        hasChanges = false;
        adjustSaveButtonState();
        adjustResetButtonState();
    }

    protected void reset(Button.ClickEvent e) {
        resetHandler.onReset(getEntity());
        hasChanges = false;
        adjustSaveButtonState();
        adjustResetButtonState();
    }

    protected void delete(Button.ClickEvent e) {
        deleteHandler.onDelete(getEntity());
        hasChanges = false;
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

    public Window openInModalPopup() {
        popup = new Window(getModalWindowTitle(), this);
        popup.setModal(true);
        UI.getCurrent().addWindow(popup);
        focusFirst();
        return popup;
    }

    /**
     * Focuses the first field found from the form. It often improves UX to call
     * this method, or focus another field, when you assign a bean for editing.
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
     *
     * @return the last Popup into which the Form was opened with
     * #openInModalPopup method or null if the form hasn't been use in window
     */
    public Window getPopup() {
        return popup;
    }
    
    public void closePopup() {
        if(getPopup() != null) {
            getPopup().close();
        }
    }

}
