/*
 * Copyright 2014 Matti Tahvonen.
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
package org.vaadin.viritin;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeNotifier;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;
import org.vaadin.viritin.fields.EagerValidateable;

import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import javax.validation.metadata.ConstraintDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enhanced version of basic BeanFieldGroup in Vaadin. Supports "eager
 * validation" and some enhancements to bean validation support.
 *
 * @param <T> the type of the bean wrapped by this group
 */
public class MBeanFieldGroup<T> extends BeanFieldGroup<T> implements
    Property.ValueChangeListener, FieldEvents.TextChangeListener {

    protected final Class nonHiddenBeanType;
    private Set<ConstraintViolation<T>> jsr303beanLevelViolations;
    private Set<Validator.InvalidValueException> beanLevelViolations;

    /**
     * Configures fields for some better defaults, like property fields
     * annotated with NotNull to be "required" (kind of a special validator in
     * Vaadin)
     */
    public void configureMaddonDefaults() {
        for (Object property : getBoundPropertyIds()) {
            final Field<?> field = getField(property);

            try {

                // Make @NotNull annotated fields "required"
                try {
                    java.lang.reflect.Field declaredField = findDeclaredField(
                        property, nonHiddenBeanType);
                    final NotNull notNullAnnotation = declaredField.
                        getAnnotation(
                            NotNull.class);
                    if (notNullAnnotation != null) {
                        field.setRequired(true);
                        Locale locale = getLocale();
                        if (locale == null) {
                            locale = Locale.getDefault();
                        }
                        String msg = getJavaxBeanValidatorFactory()
                            .getMessageInterpolator().interpolate(
                                notNullAnnotation.message(),
                                new MessageInterpolator.Context() {
                                    public ConstraintDescriptor<?> getConstraintDescriptor() {
                                        return null;
                                    }

                                    public Object getValidatedValue() {
                                        return null;
                                    }

                                    public <T> T unwrap(Class<T> type) {
                                        return null;
                                    }
                                }, locale);
                        getField(property).setRequiredError(msg);
                    }
                } catch (NoSuchFieldException ex) {
                    Logger.getLogger(MBeanFieldGroup.class.getName()).
                        log(Level.FINE, null, ex);
                } catch (SecurityException ex) {
                    Logger.getLogger(MBeanFieldGroup.class.getName()).
                        log(Level.SEVERE, null, ex);
                }
            } catch (Throwable e) {
                if (e instanceof java.lang.ClassNotFoundException) {
                    Logger.getLogger(MBeanFieldGroup.class.getName()).
                        log(Level.FINE, "Validation API not available.");
                }
            }
        }
    }

    protected java.lang.reflect.Field findDeclaredField(Object property,
                                                        Class clazz) throws NoSuchFieldException, SecurityException {
        try {
            java.lang.reflect.Field declaredField = clazz.
                getDeclaredField(property.
                    toString());
            return declaredField;
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() == null) {
                throw e;
            } else {
                return findDeclaredField(property, clazz.getSuperclass());
            }
        }
    }

    private final Set<String> fieldsWithInitiallyDisabledValidation = new HashSet<String>();

    public Set<String> getFieldsWithInitiallyDisabledValidation() {
        return Collections.
            unmodifiableSet(fieldsWithInitiallyDisabledValidation);
    }

    /**
     * This method hides validation errors on a required fields until the field
     * has been changed for the first time. Does pretty much the same as old
     * Vaadin Form did with its validationVisibleOnCommit, but eagerly per
     * field.
     * <p>
     * Fields that hide validation errors this way are available in
     * getFieldsWithIntiallyDisabledValidation() so they can be emphasized in
     * UI.
     */
    public void hideInitialEmpyFieldValidationErrors() {
        fieldsWithInitiallyDisabledValidation.clear();
        for (Field f : getFields()) {
            if (f instanceof AbstractField) {
                final AbstractField abstractField = (AbstractField) f;
                if (abstractField.getErrorMessage() != null && abstractField.
                    isRequired() && abstractField.
                    isEmpty() && abstractField.isValidationVisible()) {
                    final String propertyId = getPropertyId(abstractField).
                        toString();
                    abstractField.setValidationVisible(false);
                    fieldsWithInitiallyDisabledValidation.add(propertyId);
                }
            }
        }
    }

    /**
     * @return constraint violations found in last top level JSR303 validation.
     */
    public Set<ConstraintViolation<T>> getConstraintViolations() {
        return jsr303beanLevelViolations;
    }

    /**
     * @return constraint violations by MValidator's found in last top level
     * validation .
     */
    public Set<Validator.InvalidValueException> getBasicConstraintViolations() {
        return beanLevelViolations;
    }

    /**
     * A helper method that returns "bean level" validation errors, i.e. errors
     * that are not tied to a specific property/field.
     *
     * @return error messages from "bean level validation"
     */
    public Collection<String> getBeanLevelValidationErrors() {
        Collection<String> errors = new ArrayList<String>();
        if (getConstraintViolations() != null) {
            for (final ConstraintViolation<T> constraintViolation : getConstraintViolations()) {
                final MessageInterpolator.Context context = new MessageInterpolator.Context() {
                    @Override
                    public ConstraintDescriptor<?> getConstraintDescriptor() {
                        return constraintViolation.getConstraintDescriptor();
                    }

                    @Override
                    public Object getValidatedValue() {
                        return constraintViolation.getInvalidValue();
                    }

                    @Override
                    public <T> T unwrap(Class<T> type) {
                        throw new ValidationException();
                    }
                };

                final String msg = getJavaxBeanValidatorFactory().getMessageInterpolator().interpolate(
                    constraintViolation.getMessageTemplate(),
                    context, getLocale());
                errors.add(msg);
            }
        }
        if (getBasicConstraintViolations() != null) {
            for (Validator.InvalidValueException cv : getBasicConstraintViolations()) {
                errors.add(cv.getMessage());
            }
        }
        return errors;
    }

    // For JSR303 validation at class level
    private static ValidatorFactory factory;
    private transient javax.validation.Validator javaxBeanValidator;
    private Class<?>[] validationGroups;

    public Class<?>[] getValidationGroups() {
        if (validationGroups == null) {
            return new Class<?>[]{Default.class};
        }
        return validationGroups;
    }

    /**
     * @param validationGroups the JSR 303 bean validation groups that should be
     *                         used to validate the bean. Note, that groups currently only affect
     *                         cross-field/bean-level validation.
     */
    public void setValidationGroups(
        Class<?>... validationGroups) {
        this.validationGroups = validationGroups;
    }

    protected static ValidatorFactory getJavaxBeanValidatorFactory() {
        if (factory == null) {
            factory = Validation.buildDefaultValidatorFactory();
        }
        return factory;
    }

    protected boolean jsr303ValidateBean(T bean) {
        try {
            if (javaxBeanValidator == null) {
                javaxBeanValidator = getJavaxBeanValidatorFactory().getValidator();
            }
        } catch (Throwable t) {
            // This may happen without JSR303 validation framework
            Logger.getLogger(getClass().getName()).fine(
                "JSR303 validation failed");
            return true;
        }

        Set<ConstraintViolation<T>> constraintViolations = new HashSet(
            javaxBeanValidator.validate(bean, getValidationGroups()));
        if (constraintViolations.isEmpty()) {
            return true;
        }
        Iterator<ConstraintViolation<T>> iterator = constraintViolations.
            iterator();
        while (iterator.hasNext()) {
            ConstraintViolation<T> constraintViolation = iterator.next();
            Class<? extends Annotation> annotationType = constraintViolation.
                getConstraintDescriptor().getAnnotation().
                annotationType();
            AbstractComponent errortarget = validatorToErrorTarget.get(
                annotationType);
            if (errortarget != null) {
                // user has declared a target component for this constraint
                errortarget.setComponentError(new UserError(
                    constraintViolation.getMessage()));
                iterator.remove();
            }
            // else leave as "bean level error"
        }
        this.jsr303beanLevelViolations = constraintViolations;
        return false;
    }

    private Locale getLocale() {
        Field<?> firstField = getFields().iterator().next();
        return firstField.getLocale();
    }

    public interface FieldGroupListener<T> extends Serializable {

        public void onFieldGroupChange(MBeanFieldGroup<T> beanFieldGroup);

    }

    /**
     * EXPERIMENTAL: The cross field validation support is still experimental
     * and its API is likely to change.
     * <p>
     * A validator executed against the edited bean. Developer can do any
     * validation within the validate method, but typically this type of
     * validation are used for e.g. cross field validation which is not possible
     * with BeanValidation support in Vaadin.
     *
     * @param <T> the bean type to be validated.
     */
    public interface MValidator<T> extends Serializable {

        /**
         * @param value the bean to be validated
         * @throws Validator.InvalidValueException if value is not valid
         */
        public void validate(T value) throws Validator.InvalidValueException;

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (event != null) {
            Property property = event.getProperty();
            if (property instanceof Field) {
                Field abstractField = (Field) property;
                Object propertyId = getPropertyId(abstractField);
                if (propertyId != null) {
                    boolean wasHiddenValidation = fieldsWithInitiallyDisabledValidation.
                        remove(propertyId.toString());
                    if (wasHiddenValidation) {
                        if (abstractField instanceof AbstractField) {
                            AbstractField abstractField1 = (AbstractField) abstractField;
                            abstractField1.setValidationVisible(true);
                        }
                    }
                } else {
                    Logger.getLogger(getClass().getName()).warning(
                        "Property id for field was not found.");
                }
            }
        }
        setBeanModified(true);
        if (listener != null) {
            listener.onFieldGroupChange(this);
        }
    }

    private LinkedHashMap<MValidator<T>, Collection<AbstractComponent>> mValidators = new LinkedHashMap<MValidator<T>, Collection<AbstractComponent>>();

    /**
     * EXPERIMENTAL: The cross field validation support is still experimental
     * and its API is likely to change.
     *
     * @param validator a validator that validates the whole bean making cross
     *                  field validation much simpler
     * @param fields    the ui fields that this validator affects and on which a
     *                  possible error message is shown.
     * @return this FieldGroup
     */
    public MBeanFieldGroup<T> addValidator(MValidator<T> validator,
                                           AbstractComponent... fields) {
        mValidators.put(validator, Arrays.asList(fields));
        return this;
    }

    public MBeanFieldGroup<T> removeValidator(MValidator<T> validator) {
        mValidators.remove(validator);
        return this;
    }

    /**
     * Removes all MValidators added the MFieldGroup
     *
     * @return the instance
     */
    public MBeanFieldGroup<T> clearValidators() {
        mValidators.clear();
        return this;
    }

    private final Map<ErrorMessage, AbstractComponent> mValidationErrors = new HashMap<ErrorMessage, AbstractComponent>();

    private final Map<Class, AbstractComponent> validatorToErrorTarget = new HashMap<Class, AbstractComponent>();

    /**
     * Sets the "validation error target", the component on which validation
     * errors are shown, for given validator type.
     *
     * @param validatorType the class of the validator whose errors should be
     *                      targeted
     * @param component     the component on which the errors should be displayed on
     * @return the MBeanFieldGroup instance
     */
    public MBeanFieldGroup<T> setValidationErrorTarget(Class validatorType,
                                                       AbstractComponent component) {
        validatorToErrorTarget.put(validatorType, component);
        return this;
    }

    private void clearMValidationErrors() {
        for (AbstractComponent value : mValidationErrors.values()) {
            if (value != null) {
                value.setComponentError(null);
            }
        }
        mValidationErrors.clear();
        for (AbstractComponent ac : validatorToErrorTarget.values()) {
            ac.setComponentError(null);
        }
    }

    @Override
    public boolean isValid() {
        // clear all MValidation errors
        clearMValidationErrors();
        jsr303beanLevelViolations = null;
        beanLevelViolations = null;

        // first check standard property level validators
        final boolean propertiesValid = super.isValid();
        // then crossfield(/bean level) validators, execute them all although
        // with per field validation Vaadin checks only until the first failed one
        if (propertiesValid) {
            boolean ok = true;
            for (MValidator<T> v : mValidators.keySet()) {
                try {
                    v.validate(getItemDataSource().getBean());
                } catch (Validator.InvalidValueException e) {
                    Collection<AbstractComponent> properties = mValidators.
                        get(v);
                    if (!properties.isEmpty()) {
                        for (AbstractComponent field : properties) {
                            final ErrorMessage em = AbstractErrorMessage.
                                getErrorMessageForException(e);
                            mValidationErrors.put(em, field);
                            field.setComponentError(em);
                        }
                    } else {
                        final ErrorMessage em = AbstractErrorMessage.
                            getErrorMessageForException(e);
                        AbstractComponent target = validatorToErrorTarget.get(v.
                            getClass());
                        if (target != null) {
                            target.setComponentError(em);
                        } else {
                            // no specific "target component" for validation error
                            // leave as bean level error
                            if (beanLevelViolations == null) {
                                beanLevelViolations = new HashSet<Validator.InvalidValueException>();
                            }
                            beanLevelViolations.add(e);
                            mValidationErrors.put(em, null);
                        }
                    }
                    ok = false;
                }
            }
            return jsr303ValidateBean(getItemDataSource().getBean()) && ok;
        }
        return false;
    }

    @Override
    public void textChange(FieldEvents.TextChangeEvent event) {
        valueChange(null);
    }

    private boolean beanModified = false;
    private FieldGroupListener<T> listener;

    public void setBeanModified(boolean beanModified) {
        this.beanModified = beanModified;
    }

    public boolean isBeanModified() {
        return beanModified;
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }

    public MBeanFieldGroup(Class beanType) {
        super(beanType);
        this.nonHiddenBeanType = beanType;
    }

    public MBeanFieldGroup<T> withEagerValidation() {
        return withEagerValidation(new FieldGroupListener() {
            @Override
            public void onFieldGroupChange(MBeanFieldGroup beanFieldGroup) {
            }
        });
    }

    /**
     * Makes all fields "immediate" to trigger eager validation
     *
     * @param listener a listener that will be notified when a field in the
     *                 group has been modified
     * @return the MBeanFieldGroup that can be used for further modifications or
     * e.g. commit if buffered
     */
    public MBeanFieldGroup<T> withEagerValidation(FieldGroupListener<T> listener) {
        this.listener = listener;
        for (Field<?> field : getFields()) {
            ((AbstractComponent) field).setImmediate(true);
            field.addValueChangeListener(this);
            if (field instanceof EagerValidateable) {
                EagerValidateable ev = (EagerValidateable) field;
                ev.setEagerValidation(true);
            }
            if (field instanceof TextChangeNotifier) {
                final TextChangeNotifier abstractTextField = (TextChangeNotifier) field;
                abstractTextField.addTextChangeListener(this);
            }
        }
        return this;
    }

    /**
     * Removes all listeners from the bound fields and unbinds properties.
     */
    public void unbind() {
        // wrap in array list to avoid CME
        for (Field<?> field : new ArrayList<Field<?>>(getFields())) {
            field.removeValueChangeListener(this);
            if (field instanceof TextChangeNotifier) {
                final TextChangeNotifier abstractTextField = (TextChangeNotifier) field;
                abstractTextField.removeTextChangeListener(this);
            }

            unbind(field);
        }
        fieldsWithInitiallyDisabledValidation.clear();

    }

    /**
     * Viritin does not support buffering. Calling this function
     * will cause an UnsupportedOperationException.
     * @see <a href="https://github.com/viritin/viritin/issues/186">Issue 186</a>
     * @throws com.vaadin.data.fieldgroup.FieldGroup.CommitException
     * @deprecated
     */
    @Override
    @Deprecated
    public void commit() throws CommitException {
        Logger.getLogger(getClass().getName()).
                log(Level.WARNING, "Viritin doesn't support buffering, this "
                        + "method might not work as expected.");
        super.commit();
    }

    /**
     * Viritin does not support buffering. Calling this function
     * will cause an UnsupportedOperationException.
     * @see <a href="https://github.com/viritin/viritin/issues/186">Issue 186</a>
     * @deprecated
     */
    @Override
    @Deprecated
    public void discard() {
        Logger.getLogger(getClass().getName()).
                log(Level.WARNING, "Viritin doesn't support buffering, this "
                        + "method might not work as expected.");
        super.discard();
    }

    /**
     * Viritin does not support buffering. Calling this function with buffered=true
     * will cause an UnsupportedOperationException.
     * @see <a href="https://github.com/viritin/viritin/issues/186">Issue 186</a>
     * @deprecated
     */
    @Override
    @Deprecated
    public void setBuffered(boolean buffered) {
        if (buffered == true) {
            Logger.getLogger(getClass().getName()).
                    log(Level.WARNING, "Viritin doesn't support buffering, this "
                            + "method might not work as expected.");
        }
        super.setBuffered(buffered);
    }

    /**
     * Configures a field with the settings set for this FieldBinder.
     * <p>
     * By default this updates the buffered, read only and enabled state of the
     * field. Also adds validators when applicable. Fields with read only data
     * source are always configured as read only.
     * <p>
     * Unlike the default implementation in FieldGroup, MBeanFieldGroup only
     * makes field read only based on the property's hint, not the opposite.
     * This way developer can in form code choose to make some fields read only.
     *
     * @param field The field to update
     */
    @Override
    protected void configureField(Field<?> field) {
        field.setBuffered(isBuffered());

        field.setEnabled(isEnabled());
        
        if(isReadOnly() || field.getPropertyDataSource().isReadOnly()) {
            field.setReadOnly(true);
        }
    }


    private static final String NO_BUFFERING_SUPPORT = "Buffering is not supported by Viritin. "
            + "Please, see https://github.com/viritin/viritin/issues/186 for details.";

}
