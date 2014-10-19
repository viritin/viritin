package org.vaadin.maddon.form;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.maddon.BeanBinder;
import org.vaadin.maddon.MBeanFieldGroup;
import org.vaadin.maddon.MBeanFieldGroup.FieldGroupListener;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.button.PrimaryButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;

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
        setCompositionRoot(createContent());
        adjustSaveButtonState();
        adjustCancelButtonState();
    }

    private MBeanFieldGroup<T> fieldGroup;

    @Override
    public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
        adjustSaveButtonState();
        adjustCancelButtonState();
    }

    protected void adjustSaveButtonState() {
        if (isAttached() && isEagarValidation() && isBound()) {
            boolean beanModified = fieldGroup.isBeanModified();
            boolean valid = fieldGroup.isValid();
            getSaveButton().setEnabled(beanModified && valid);
        }
    }

    protected boolean isBound() {
        return fieldGroup != null;
    }

    private void adjustCancelButtonState() {
        if (isAttached() && isEagarValidation() && isBound()) {
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

    private T entity;
    private SavedHandler<T> savedHandler;
    private ResetHandler<T> resetHandler;
    private boolean eagarValidation;

    public boolean isEagarValidation() {
        return eagarValidation;
    }

    /**
     * In case one is working with "detached entities" enabling eager validation
     * will highly improve usability. The validity of the form will be updated
     * on each changes and save/cancel buttons will reflect to the validity and
     * possible changes.
     *
     * @param eagarValidation true if the form should have eager validation
     */
    public void setEagarValidation(boolean eagarValidation) {
        this.eagarValidation = eagarValidation;
    }

    public MBeanFieldGroup<T> setEntity(T entity) {
        this.entity = entity;
        if (entity != null) {
            if (isBound()) {
                fieldGroup.unbind();
            }
            fieldGroup = BeanBinder.bind(entity, this);
            if (isEagarValidation()) {
                fieldGroup.withEagarValidation(this);
                adjustSaveButtonState();
                adjustCancelButtonState();
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
    }

    public void setResetHandler(ResetHandler<T> resetHandler) {
        this.resetHandler = resetHandler;
    }

    public ResetHandler<T> getResetHandler() {
        return resetHandler;
    }

    public SavedHandler<T> getSavedHandler() {
        return savedHandler;
    }

    public Window openInModalPopup() {
        Window window = new Window("Edit entry", this);
        window.setModal(true);
        UI.getCurrent().addWindow(window);
        focusFirst();
        return window;
    }

    /**
     * @return A default toolbar containing save/cancel buttons
     */
    public HorizontalLayout getToolbar() {
        return new MHorizontalLayout(
                getSaveButton(),
                getResetButton()
        );
    }

    protected Button createCancelButton() {
        return new MButton("Cancel");
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
        return new PrimaryButton("Save");
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

    protected void save(Button.ClickEvent e) {
        savedHandler.onSave(entity);
    }

    protected void reset(Button.ClickEvent e) {
        resetHandler.onReset(entity);
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

}
