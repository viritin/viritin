package org.vaadin.viritin.fields;

import com.vaadin.data.Buffered;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.themes.ValoTheme;
import java.util.Collection;
import java.util.EventObject;
import java.util.Locale;
import java.util.Map;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.util.HtmlElementPropertySetter;

/**
 * A textfield with an attached button, that clears the textfield. Most methods in this class delegate to the textfield,
 * some to the button. ClearableTextField should work as a drop-in replacement for TextFiled / MTextField.
 *
 * @author Niki
 */
public class ClearableTextField extends CustomComponent implements Field {

    private final MTextField textfield = new MTextField();
    private final CssLayout root = new CssLayout();
    private final MButton clearButton = new MButton();

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public ClearableTextField() {
        clearButton.setIcon(FontAwesome.TIMES);
        clearButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                textfield.clear();
                textfield.focus();
            }
        });

        root.addComponents(textfield, clearButton);
        root.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        setCompositionRoot(root);
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
     * @return The TextField
     */
    public MTextField getTextfield() {
        return textfield;
    }

    /**
     *
     * @return The root CssLayout
     */
    public CssLayout getRoot() {
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
    public boolean isReadOnly() {
        return textfield.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        textfield.setReadOnly(readOnly);
    }

    public boolean isInvalidCommitted() {
        return textfield.isInvalidCommitted();
    }

    public void setInvalidCommitted(boolean isCommitted) {
        textfield.setInvalidCommitted(isCommitted);
    }

    public void commit() throws Buffered.SourceException, Validator.InvalidValueException {
        textfield.commit();
    }

    public void discard() throws Buffered.SourceException {
        textfield.discard();
    }

    public boolean isModified() {
        return textfield.isModified();
    }

    public void setBuffered(boolean buffered) {
        textfield.setBuffered(buffered);
    }

    public boolean isBuffered() {
        return textfield.isBuffered();
    }

    public String getValue() {
        return textfield.getValue();
    }

    public Property getPropertyDataSource() {
        return textfield.getPropertyDataSource();
    }

    public void setPropertyDataSource(Property newDataSource) {
        textfield.setPropertyDataSource(newDataSource);
    }

    public void setConverter(Class<?> datamodelType) {
        textfield.setConverter(datamodelType);
    }

    public Object getConvertedValue() {
        return textfield.getConvertedValue();
    }

    public void setConvertedValue(Object value) {
        textfield.setConvertedValue(value);
    }

    public void addValidator(Validator validator) {
        textfield.addValidator(validator);
    }

    public Collection<Validator> getValidators() {
        return textfield.getValidators();
    }

    public void removeValidator(Validator validator) {
        textfield.removeValidator(validator);
    }

    public void removeAllValidators() {
        textfield.removeAllValidators();
    }

    public boolean isInvalidAllowed() {
        return textfield.isInvalidAllowed();
    }

    public void setInvalidAllowed(boolean invalidAllowed) throws UnsupportedOperationException {
        textfield.setInvalidAllowed(invalidAllowed);
    }

    public void addValueChangeListener(Property.ValueChangeListener listener) {
        textfield.addValueChangeListener(listener);
    }

    public void removeValueChangeListener(Property.ValueChangeListener listener) {
        textfield.removeValueChangeListener(listener);
    }

    public void readOnlyStatusChange(Property.ReadOnlyStatusChangeEvent event) {
        textfield.readOnlyStatusChange(event);
    }

    public void addReadOnlyStatusChangeListener(Property.ReadOnlyStatusChangeListener listener) {
        textfield.addReadOnlyStatusChangeListener(listener);
    }

    public void removeReadOnlyStatusChangeListener(Property.ReadOnlyStatusChangeListener listener) {
        textfield.removeReadOnlyStatusChangeListener(listener);
    }

    public int getTabIndex() {
        return textfield.getTabIndex();
    }

    public void setTabIndex(int tabIndex) {
        textfield.setTabIndex(tabIndex);
    }

    @Override
    public void setLocale(Locale locale) {
        textfield.setLocale(locale);
    }

    public boolean isRequired() {
        return textfield.isRequired();
    }

    public void setRequired(boolean required) {
        textfield.setRequired(required);
    }

    public void setRequiredError(String requiredMessage) {
        textfield.setRequiredError(requiredMessage);
    }

    public String getRequiredError() {
        return textfield.getRequiredError();
    }

    public String getConversionError() {
        return textfield.getConversionError();
    }

    public void setConversionError(String valueConversionError) {
        textfield.setConversionError(valueConversionError);
    }

    public boolean isValidationVisible() {
        return textfield.isValidationVisible();
    }

    public void setValidationVisible(boolean validateAutomatically) {
        textfield.setValidationVisible(validateAutomatically);
    }

    public void setCurrentBufferedSourceException(Buffered.SourceException currentBufferedSourceException) {
        textfield.setCurrentBufferedSourceException(currentBufferedSourceException);
    }

    public Converter<String, Object> getConverter() {
        return textfield.getConverter();
    }

    public void setConverter(Converter<String, ?> converter) {
        textfield.setConverter(converter);
    }

    @Override
    public boolean isImmediate() {
        return textfield.isImmediate();
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

    public void clear() {
        textfield.clear();
    }

    public void paintContent(PaintTarget target) throws PaintException {
        textfield.paintContent(target);
    }

    public void changeVariables(Object source, Map<String, Object> variables) {
        textfield.changeVariables(source, variables);
    }

    public Class<String> getType() {
        return textfield.getType();
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

    public boolean isEmpty() {
        return textfield.isEmpty();
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

    public void setValue(String newValue) throws Property.ReadOnlyException {
        textfield.setValue(newValue);
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

    public void addFocusListener(FieldEvents.FocusListener listener) {
        textfield.addFocusListener(listener);
    }

    public void removeFocusListener(FieldEvents.FocusListener listener) {
        textfield.removeFocusListener(listener);
    }

    public void addBlurListener(FieldEvents.BlurListener listener) {
        textfield.addBlurListener(listener);
    }

    public void removeBlurListener(FieldEvents.BlurListener listener) {
        textfield.removeBlurListener(listener);
    }

    public MTextField withCaption(String caption) {
        return textfield.withCaption(caption);
    }

    protected void setValue(String newFieldValue, boolean repaintIsNotNeeded) throws Property.ReadOnlyException, Converter.ConversionException, Validator.InvalidValueException {
        textfield.setValue(newFieldValue, repaintIsNotNeeded);
    }

    public boolean isEagerValidation() {
        return textfield.isEagerValidation();
    }

    public void setEagerValidation(boolean eagerValidation) {
        textfield.setEagerValidation(eagerValidation);
    }

    @Override
    protected void fireEvent(EventObject event) {
        textfield.fireEvent(event);
    }

    public String getLastKnownTextContent() {
        return textfield.getLastKnownTextContent();
    }

    public MTextField withConversionError(String message) {
        return textfield.withConversionError(message);
    }

    public MTextField withConverter(Converter<String, ?> converter) {
        return textfield.withConverter(converter);
    }

    public MTextField withFullWidth() {
        return textfield.withFullWidth();
    }

    public MTextField withValue(String value) {
        return textfield.withValue(value);
    }

    public MTextField withInputPrompt(String inputPrompt) {
        return textfield.withInputPrompt(inputPrompt);
    }

    public MTextField withReadOnly(boolean readOnly) {
        return textfield.withReadOnly(readOnly);
    }

    public MTextField withValidator(Validator validator) {
        return textfield.withValidator(validator);
    }

    public MTextField withWidth(float width, Unit unit) {
        return textfield.withWidth(width, unit);
    }

    public MTextField withWidth(String width) {
        return textfield.withWidth(width);
    }

    public MTextField withNullRepresentation(String nullRepresentation) {
        return textfield.withNullRepresentation(nullRepresentation);
    }

    public MTextField withStyleName(String... styleNames) {
        return textfield.withStyleName(styleNames);
    }

    public MTextField withIcon(Resource icon) {
        return textfield.withIcon(icon);
    }

    public MTextField withRequired(boolean required) {
        return textfield.withRequired(required);
    }

    public MTextField withRequiredError(String requiredError) {
        return textfield.withRequiredError(requiredError);
    }

    public MTextField withVisible(boolean visible) {
        return textfield.withVisible(visible);
    }

    public MTextField withTextChangeListener(FieldEvents.TextChangeListener listener) {
        return textfield.withTextChangeListener(listener);
    }

    public MTextField withValueChangeListener(Property.ValueChangeListener listener) {
        return textfield.withValueChangeListener(listener);
    }

    public MTextField withBlurListener(FieldEvents.BlurListener listener) {
        return textfield.withBlurListener(listener);
    }

    public void setSpellcheck(Boolean spellcheck) {
        textfield.setSpellcheck(spellcheck);
    }

    public Boolean getSpellcheck() {
        return textfield.getSpellcheck();
    }

    public MTextField withSpellCheckOff() {
        return textfield.withSpellCheckOff();
    }

    public MTextField withId(String id) {
        return textfield.withId(id);
    }

    public MTextField withAutocompleteOff() {
        return textfield.withAutocompleteOff();
    }

    public MTextField setAutocomplete(MTextField.AutoComplete autocomplete) {
        return textfield.setAutocomplete(autocomplete);
    }

    public MTextField.AutoComplete getAutocomplete() {
        return textfield.getAutocomplete();
    }

    public MTextField withAutoCapitalizeOff() {
        return textfield.withAutoCapitalizeOff();
    }

    public MTextField setAutoCapitalize(MTextField.AutoCapitalize autoCapitalize) {
        return textfield.setAutoCapitalize(autoCapitalize);
    }

    public MTextField.AutoCapitalize getAutoCapitalize() {
        return textfield.getAutoCapitalize();
    }

    public MTextField withAutoCorrectOff() {
        return textfield.withAutoCorrectOff();
    }

    public MTextField setAutoCorrect(MTextField.AutoCorrect autoCorrect) {
        return textfield.setAutoCorrect(autoCorrect);
    }

    public MTextField.AutoCorrect getAutoCorrect() {
        return textfield.getAutoCorrect();
    }

    protected HtmlElementPropertySetter getHtmlElementPropertySetter() {
        return textfield.getHtmlElementPropertySetter();
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        textfield.beforeClientResponse(initial);
    }

    @Override
    public ErrorMessage getErrorMessage() {
        return textfield.getErrorMessage();
    }

    protected Validator.InvalidValueException getValidationError() {
        return textfield.getValidationError();
    }

    protected void doEagerValidation() {
        textfield.doEagerValidation();
    }

    public void valueChange(Property.ValueChangeEvent event) {
        textfield.valueChange(event);
    }

    public boolean isValid() {
        return textfield.isValid();
    }

    public void validate() throws Validator.InvalidValueException {
        textfield.validate();
    }

    @Override
    public void setValue(Object newValue) throws Property.ReadOnlyException {
        if (newValue != null) {
            this.setValue(newValue.toString());
        } else {
            this.setValue(null);
        }
    }

    @Override
    @Deprecated
    public void addListener(Property.ValueChangeListener listener) {
        textfield.addListener(listener);
    }

    @Override
    @Deprecated
    public void removeListener(Property.ValueChangeListener listener) {
        textfield.removeListener(listener);
    }

    @Override
    public void focus() {
        super.focus();
    }
}
