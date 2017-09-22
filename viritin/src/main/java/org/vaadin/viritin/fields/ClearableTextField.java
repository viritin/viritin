package org.vaadin.viritin.fields;

import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A textfield with an attached button, that clears the textfield. Most methods
 * in this class delegate to the textfield, some to the button.
 * ClearableTextField should work as a drop-in replacement for TextFiled /
 * MTextField.
 *
 * @author Niki
 */
public class ClearableTextField extends CustomField {

    private final TextField textfield = new TextField();
    private final MButton clearButton = new MButton(VaadinIcons.CLOSE)
            .withStyleName(ValoTheme.BUTTON_ICON_ONLY);
    private final MHorizontalLayout root = new MHorizontalLayout()
            .expand(textfield).add(clearButton)
            .withSpacing(false)
            .withFullWidth()
            .withStyleName("clearable-textfield");

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ClearableTextField() {
        clearButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                textfield.clear();
                textfield.focus();
            }
        });
        
        textfield.addValueChangeListener(new ValueChangeListener<String>() {
            @Override
            public void valueChange(ValueChangeEvent<String> event) {
                setValue(event.getValue());
            }
        });

        textfield.setValueChangeMode(ValueChangeMode.LAZY);

        setWidth("300px");
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ClearableTextField(String caption) {
        this();
        this.setCaption(caption);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ClearableTextField(String caption, String value) {
        this();
        this.setCaption(caption);
        textfield.setValue(value);
    }

    /**
     *
     * @return The TextField from the composition
     */
    public TextField getTextfield() {
        return textfield;
    }

    /**
     *
     * @return The root CssLayout
     */
    public Layout getRoot() {
        return root;
    }

    /**
     *
     * @return The Button
     */
    public MButton getClearButton() {
        return clearButton;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        textfield.setReadOnly(readOnly);
        clearButton.setEnabled(!readOnly);
    }

    /**
     * A method to add custom click listener to the clear button.
     *
     * @return the listener registration
     * @see Button#addClickListener(Button.ClickListener listener)
     * @param listener the listener to be added to the clear button
     */
    public Registration addClickListener(Button.ClickListener listener) {
        return clearButton.addClickListener(listener);
    }

    public void click() {
        clearButton.click();
    }

    public void setClickShortcut(int keyCode, int... modifiers) {
        clearButton.setClickShortcut(keyCode, modifiers);
    }

    public void removeClickShortcut() {
        clearButton.removeClickShortcut();
    }

    public void setIcon(Resource icon, String iconAltText) {
        clearButton.setIcon(icon, iconAltText);
    }

    public String getIconAlternateText() {
        return clearButton.getIconAlternateText();
    }

    public void setIconAlternateText(String iconAltText) {
        clearButton.setIconAlternateText(iconAltText);
    }

    public void setHtmlContentAllowed(boolean htmlContentAllowed) {
        clearButton.setHtmlContentAllowed(htmlContentAllowed);
    }

    public boolean isHtmlContentAllowed() {
        return clearButton.isHtmlContentAllowed();
    }

    @Override
    public void clear() {
        super.clear();
        textfield.clear();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    public int getMaxLength() {
        return textfield.getMaxLength();
    }

    public void setMaxLength(int maxLength) {
        textfield.setMaxLength(maxLength);
    }

    public String getPlaceholder() {
        return textfield.getPlaceholder();
    }

    public void setPlaceholder(String inputPrompt) {
        textfield.setPlaceholder(inputPrompt);
    }

    public void selectAll() {
        textfield.selectAll();
    }

    public void setSelection(int pos, int length) {
        textfield.setSelection(pos, length);
    }

    public void setCursorPosition(int pos) {
        textfield.setCursorPosition(pos);
    }

    public int getCursorPosition() {
        return textfield.getCursorPosition();
    }

    public Registration addFocusListener(FocusListener listener) {
        return textfield.addFocusListener(listener);
    }

    public Registration addBlurListener(BlurListener listener) {
        return textfield.addBlurListener(listener);
    }

    public ClearableTextField withCaption(String caption) {
        super.setCaption(caption);
        return this;
    }

    public ClearableTextField withFullWidth() {
        this.setWidth("100%");
        return this;
    }

    public ClearableTextField withValue(String value) {
        setValue(value);
        return this;
    }

    @Override
    public ErrorMessage getErrorMessage() {
        final ErrorMessage errorMessage = super.getErrorMessage();
        if (errorMessage == null) {
            textfield.removeStyleName("error");
        } else {
            textfield.addStyleName("error");
        }
        return errorMessage;
    }

    @Override
    public void setComponentError(ErrorMessage componentError) {
        super.setComponentError(componentError);
        if (componentError == null) {
            textfield.removeStyleName("error");
        } else {
            textfield.addStyleName("error");
        }
    }

    @Override
    public void focus() {
        textfield.focus();
    }

    @Override
    protected Component initContent() {
        return root;
    }

    @Override
    public void attach() {
        super.attach();
        // TODO optimize this so that it is added only once
        Page.getCurrent().getStyles().add(
                ".clearable-textfield .v-widget {\n"
                + "	border-radius: 4px 4px 4px 4px;\n"
                + "}\n"
                + ".clearable-textfield .v-slot:last-child>.v-widget {\n"
                + "	border-top-left-radius: 0;\n"
                + "	border-bottom-left-radius: 0; margin-left:-1px\n"
                + "}\n"
                + "\n"
                + ".clearable-textfield .v-slot:first-child>.v-widget {\n"
                + "	border-top-right-radius: 0;\n"
                + "	border-bottom-right-radius: 0;\n"
                + "}\n");
    }

    @Override
    protected void doSetValue(Object newValue) {
        textfield.setValue(newValue.toString());
    }

    @Override
    public Object getValue() {
        return textfield.getValue();
    }

}
