package org.vaadin.viritin.fields;

import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.event.FieldEvents;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.v7.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.CustomField;
import com.vaadin.v7.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * A textfield with an attached button, that clears the textfield. Most methods
 * in this class delegate to the textfield, some to the button.
 * ClearableTextField should work as a drop-in replacement for TextFiled /
 * MTextField.
 *
 * @author Niki
 */
public class ClearableTextField extends CustomField {

    private final MTextField textfield = new MTextField();
    private final MButton clearButton = new MButton(FontAwesome.TIMES).withStyleName(ValoTheme.BUTTON_ICON_ONLY);
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

        textfield.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                setValue(textfield.getValue());
            }
        });

        textfield.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                setValue(event.getText());
            }
        });

        setWidth("300px");
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ClearableTextField(String caption) {
        this();
        this.setCaption(caption);
    }

    public ClearableTextField(Property dataSource) {
        this();
        textfield.setPropertyDataSource(dataSource);

    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ClearableTextField(String caption, Property dataSource) {
        this();
        this.setCaption(caption);
        textfield.setPropertyDataSource(dataSource);

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
    public MTextField getTextfield() {
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

    @Override
    public void setImmediate(boolean immediate) {
        super.setImmediate(immediate);
        textfield.setImmediate(immediate);
    }

    /**
     * A method to add custom click listener to the clear button.
     *
     * @see Button#addClickListener(Button.ClickListener listener)
     * @param listener the listener to be added to the clear button
     */
    public void addClickListener(Button.ClickListener listener) {
        clearButton.addClickListener(listener);
    }

    public void removeClickListener(Button.ClickListener listener) {
        clearButton.removeClickListener(listener);
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

    public String getNullRepresentation() {
        return textfield.getNullRepresentation();
    }

    public boolean isNullSettingAllowed() {
        return textfield.isNullSettingAllowed();
    }

    public void setNullRepresentation(String nullRepresentation) {
        textfield.setNullRepresentation(nullRepresentation);
    }

    public void setNullSettingAllowed(boolean nullSettingAllowed) {
        textfield.setNullSettingAllowed(nullSettingAllowed);
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

    public int getColumns() {
        return textfield.getColumns();
    }

    public void setColumns(int columns) {
        textfield.setColumns(columns);
    }

    public String getInputPrompt() {
        return textfield.getInputPrompt();
    }

    public void setInputPrompt(String inputPrompt) {
        textfield.setInputPrompt(inputPrompt);
    }

    public void setTextChangeEventMode(AbstractTextField.TextChangeEventMode inputEventMode) {
        textfield.setTextChangeEventMode(inputEventMode);
    }

    public AbstractTextField.TextChangeEventMode getTextChangeEventMode() {
        return textfield.getTextChangeEventMode();
    }

    public void addTextChangeListener(FieldEvents.TextChangeListener listener) {
        textfield.addTextChangeListener(listener);
    }

    public void removeTextChangeListener(FieldEvents.TextChangeListener listener) {
        textfield.removeTextChangeListener(listener);
    }

    public void setTextChangeTimeout(int timeout) {
        textfield.setTextChangeTimeout(timeout);
    }

    public int getTextChangeTimeout() {
        return textfield.getTextChangeTimeout();
    }

    public void selectAll() {
        textfield.selectAll();
    }

    public void setSelectionRange(int pos, int length) {
        textfield.setSelectionRange(pos, length);
    }

    public void setCursorPosition(int pos) {
        textfield.setCursorPosition(pos);
    }

    public int getCursorPosition() {
        return textfield.getCursorPosition();
    }

    public void addFocusListener(FocusListener listener) {
        textfield.addFocusListener(listener);
    }

    public void removeFocusListener(FocusListener listener) {
        textfield.removeFocusListener(listener);
    }

    public void addBlurListener(BlurListener listener) {
        textfield.addBlurListener(listener);
    }

    public void removeBlurListener(BlurListener listener) {
        textfield.removeBlurListener(listener);
    }

    public ClearableTextField withCaption(String caption) {
        super.setCaption(caption);
        return this;
    }

    public ClearableTextField withConversionError(String message) {
        setConversionError(message);
        return this;
    }

    public ClearableTextField withConverter(Converter<String, ?> converter) {
        setConverter(converter);
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

    protected void doEagerValidation() {
        textfield.doEagerValidation();
    }

    @Override
    protected void setInternalValue(Object newValue) {
        super.setInternalValue(newValue);
        textfield.setValue(newValue.toString());
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
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial); //To change body of generated methods, choose Tools | Templates.
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
    public Class getType() {
        return String.class;
    }

    @Override
    public void attach() {
        super.attach();
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

}
