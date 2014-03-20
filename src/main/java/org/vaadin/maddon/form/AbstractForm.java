package org.vaadin.maddon.form;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.maddon.BeanBinder;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.button.PrimaryButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * Abstract super class for simple editor forms.
 *
 * @param <T> the type of the bean edited
 */
public abstract class AbstractForm<T> extends CustomComponent {

    public interface SavedHandler<T> {

        void onSave(T entity);
    }

    public interface ResetHandler<T> {

        void onReset(T entity);
    }

    private T entity;
    private SavedHandler<T> savedHandler;
    private ResetHandler<T> resetHandler;

    public void setEntity(T entity) {
        this.entity = entity;
        if (entity != null) {
            BeanBinder.bind(entity, this);
            setVisible(true);
        } else {
            setVisible(false);
        }
    }

    public void setSavedHandler(SavedHandler<T> savedHandler) {
        this.savedHandler = savedHandler;
    }

    public void setResetHandler(ResetHandler<T> resetHandler) {
        this.resetHandler = resetHandler;
    }

    /**
     * @return A default toolbar containing save & cancel buttons
     */
    public HorizontalLayout getToolbar() {
        return new MHorizontalLayout(
                createSaveButton(),
                createCancelButton()
        );
    }

    protected Component createCancelButton() {
        return new MButton("Cancel", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                reset(event);
            }
        });
    }

    protected Component createSaveButton() {
        return new PrimaryButton("Save", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                save(event);
            }
        });
    }

    protected void save(Button.ClickEvent e) {
        savedHandler.onSave(entity);
    }

    protected void reset(Button.ClickEvent e) {
        resetHandler.onReset(entity);
    }

    @Override
    public void attach() {
        setCompositionRoot(createContent());
        super.attach();
    }

    /**
     * This method should return the actual content of the form, including
     * possible toolbar.
     *
     * Example implementation could look like this:
     * <code>
     * public class PersonForm extends AbstractForm&lt;Person&gt; {
     * 
     *     private TextField firstName = new MTextField(&quot;First Name&quot;);
     *     private TextField lastName = new MTextField(&quot;Last Name&quot;);
     * 
     *     @Override
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
