/*
 * Copyright 2014 mattitahvonenitmill.
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
package org.vaadin.viritin.fields;

import com.vaadin.event.FieldEvents.BlurListener;
import java.util.EventObject;

import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.Validator;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.v7.event.FieldEvents;
import com.vaadin.v7.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Resource;
import com.vaadin.v7.ui.PasswordField;

/**
 * A an extension to basic Vaadin PasswordField. Uses the only sane default for
 * "nullRepresentation" (""), adds support for "eager validation" (~ validate
 * while typing) and adds some fluent APIs.
 */
public class MPasswordField extends PasswordField implements EagerValidateable {

    private static final long serialVersionUID = 7493214285444996814L;
    
    private boolean eagerValidation = false;
    private boolean eagerValidationStatus;
    private String lastKnownTextChangeValue;
    private Validator.InvalidValueException eagerValidationError;

    public MPasswordField() {
        configureMaddonStuff();
    }

    private void configureMaddonStuff() {
        setNullRepresentation("");
    }

    public MPasswordField(String caption) {
        super(caption);
        configureMaddonStuff();
    }

    public MPasswordField(Property dataSource) {
        super(dataSource);
        configureMaddonStuff();
    }

    public MPasswordField(String caption, Property dataSource) {
        super(caption, dataSource);
        configureMaddonStuff();
    }

    public MPasswordField(String caption, String value) {
        super(caption, value);
    }

    @Override
    protected void setValue(String newFieldValue, boolean repaintIsNotNeeded) throws ReadOnlyException, Converter.ConversionException, Validator.InvalidValueException {
        lastKnownTextChangeValue = null;
        eagerValidationError = null;
        super.setValue(newFieldValue, repaintIsNotNeeded);
    }

    @Override
    public boolean isEagerValidation() {
        return eagerValidation;
    }

    @Override
    public void setEagerValidation(boolean eagerValidation) {
        this.eagerValidation = eagerValidation;
    }

    @Override
    protected void fireEvent(EventObject event) {
        if (isEagerValidation() && event instanceof TextChangeEvent) {
            lastKnownTextChangeValue = ((TextChangeEvent) event).getText();
            doEagerValidation();
        }
        super.fireEvent(event);
    }

    /**
     *
     * @return the value of the field or if a text change event have sent a
     * value to the server since last value changes, then that.
     */
    public String getLastKnownTextContent() {
        return lastKnownTextChangeValue;
    }

    public MPasswordField withConversionError(String message) {
        setConversionError(message);
        return this;
    }

    public MPasswordField withConverter(Converter<String, ?> converter) {
        setConverter(converter);
        return this;
    }

    public MPasswordField withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MPasswordField withInputPrompt(String inputPrompt) {
        setInputPrompt(inputPrompt);
        return this;
    }

    public MPasswordField withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }

    public MPasswordField withValidator(Validator validator) {
        addValidator(validator);
        return this;
    }

    public MPasswordField withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MPasswordField withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MPasswordField withNullRepresentation(String nullRepresentation) {
        setNullRepresentation(nullRepresentation);
        return this;
    }

    public MPasswordField withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    public MPasswordField withRequired(boolean required) {
        setRequired(required);
        return this;
    }

    public MPasswordField withRequiredError(String requiredError) {
        setRequiredError(requiredError);
        return this;
    }

    public MPasswordField withId(String id) {
        setId(id);
        return this;
    }

    public MPasswordField withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    public MPasswordField withTextChangeListener(FieldEvents.TextChangeListener listener) {
        addTextChangeListener(listener);
        return this;
    }

    public MPasswordField withValueChangeListener(Property.ValueChangeListener listener) {
        addValueChangeListener(listener);
        return this;
    }

    public MPasswordField withBlurListener(BlurListener listener) {
        addBlurListener(listener);
        return this;
    }

    @Override
    public ErrorMessage getErrorMessage() {

        Validator.InvalidValueException validationError = getValidationError();

        final ErrorMessage superError = getComponentError();

        if (superError == null && validationError == null
                && getCurrentBufferedSourceException() == null) {
            return null;
        }
        // Throw combination of the error types
        return new CompositeErrorMessage(
                new ErrorMessage[]{
                    superError,
                    AbstractErrorMessage
                    .getErrorMessageForException(validationError),
                    AbstractErrorMessage
                    .getErrorMessageForException(
                            getCurrentBufferedSourceException())});
    }

    protected Validator.InvalidValueException getValidationError() {
        if (isEagerValidation() && lastKnownTextChangeValue != null) {
            return eagerValidationError;
        }
        /*
         * Check validation errors only if automatic validation is enabled.
         * Empty, required fields will generate a validation error containing
         * the requiredError string. For these fields the exclamation mark will
         * be hidden but the error must still be sent to the client.
         */
        Validator.InvalidValueException validationError = null;
        if (isValidationVisible()) {
            try {
                validate();
            } catch (Validator.InvalidValueException e) {
                if (!e.isInvisible()) {
                    validationError = e;
                }
            }
        }
        return validationError;
    }

    protected void doEagerValidation() {
        final boolean wasvalid = eagerValidationStatus;
        eagerValidationStatus = true;
        eagerValidationError = null;
        try {
            if (isRequired() && getLastKnownTextContent().isEmpty()) {
                throw new Validator.EmptyValueException(getRequiredError());
            }
            validate(getLastKnownTextContent());
            if (!wasvalid) {
                markAsDirty();
            }
        } catch (Validator.InvalidValueException e) {
            eagerValidationError = e;
            eagerValidationStatus = false;
            markAsDirty();
        }
    }

    @Override
    public boolean isValid() {
        if (isEagerValidation() && lastKnownTextChangeValue != null) {
            return eagerValidationStatus;
        } else {
            return super.isValid();
        }
    }

    @Override
    public void validate() throws Validator.InvalidValueException {
        if (isEagerValidation() && lastKnownTextChangeValue != null) {
            // This is most likely not executed, unless someone, for some weird
            // reason calls this explicitly
            if (isRequired() && getLastKnownTextContent().isEmpty()) {
                throw new Validator.EmptyValueException(getRequiredError());
            }
            validate(getLastKnownTextContent());
        } else {
            super.validate();
        }
    }

}
