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
package org.vaadin.maddon.fields;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.CompositeErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.TextField;
import java.util.EventObject;

/**
 *
 * @author mattitahvonenitmill
 */
public class MTextField extends TextField {

    private boolean eagerValidation = false;
    private boolean eagerValidationStatus;
    private String lastKnownTextChangeValue;
    private Validator.InvalidValueException eagerValidationError;

    public MTextField() {
        configureMaddonStuff();
    }

    private void configureMaddonStuff() {
        setNullRepresentation("");
    }

    public MTextField(String caption) {
        super(caption);
        configureMaddonStuff();
    }

    public MTextField(Property dataSource) {
        super(dataSource);
        configureMaddonStuff();
    }

    public MTextField(String caption, Property dataSource) {
        super(caption, dataSource);
        configureMaddonStuff();
    }

    public MTextField(String caption, String value) {
        super(caption, value);
    }

    @Override
    protected void setValue(String newFieldValue, boolean repaintIsNotNeeded) throws ReadOnlyException, Converter.ConversionException, Validator.InvalidValueException {
        lastKnownTextChangeValue = null;
        eagerValidationError = null;
        super.setValue(newFieldValue, repaintIsNotNeeded);
    }

    public boolean isEagerValidation() {
        return eagerValidation;
    }

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

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        super.addValueChangeListener(listener);
        // Remove once 7.2 is out
        setImmediate(true);
    }

    public MTextField withConversionError(String message) {
        setImmediate(true);
        setConversionError(message);
        return this;
    }

    public MTextField withConverter(Converter<String, ?> converter) {
        setImmediate(true);
        setConverter(converter);
        return this;
    }

    public MTextField withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MTextField withInputPrompt(String inputPrompt) {
        setInputPrompt(inputPrompt);
        return this;
    }

    public MTextField withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }

    public MTextField withValidator(Validator validator) {
        setImmediate(true);
        addValidator(validator);
        return this;
    }

    public MTextField withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MTextField withWidth(String width) {
        setWidth(width);
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
                new ErrorMessage[] {
                        superError,
                        AbstractErrorMessage
                                .getErrorMessageForException(validationError),
                        AbstractErrorMessage
                                .getErrorMessageForException(getCurrentBufferedSourceException()) });
    }

    protected Validator.InvalidValueException getValidationError() {
        if(isEagerValidation() && lastKnownTextChangeValue != null) {
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
            if(!wasvalid) {
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
