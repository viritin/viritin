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
import com.vaadin.event.*;
import com.vaadin.event.FieldEvents.TextChangeNotifier;
import com.vaadin.server.AbstractErrorMessage;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.constraints.NotNull;
import org.vaadin.viritin.fields.MPasswordField;

import org.vaadin.viritin.fields.MTextField;

/**
 * Enhanced version of basic BeanFieldGroup in Vaadin. Supports "eager
 * validation" and some enhancements to bean validation support.
 *
 * @param <T> the type of the bean wrapped by this group
 */
public class MBeanFieldGroup<T> extends BeanFieldGroup<T> implements
        Property.ValueChangeListener, FieldEvents.TextChangeListener {

    protected final Class nonHiddenBeanType;

    /**
     * Configures fields for some better defaults, like property fields
     * annotated with NotNull to be "required" (kind of a special validator in
     * Vaadin)
     */
    public void configureMaddonDefaults() {
        for (Object property : getBoundPropertyIds()) {
            final Field<?> field = getField(property);

            // Make @NotNull annotated fields "required"
            try {
                java.lang.reflect.Field declaredField = findDeclaredField(
                        property, nonHiddenBeanType);
                final NotNull notNullAnnotation = declaredField.getAnnotation(
                        NotNull.class);
                if (notNullAnnotation != null) {
                    field.setRequired(true);
                    if (notNullAnnotation.message() != null) {
                        getField(property).setRequiredError(notNullAnnotation.
                                message());
                    }
                }
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(MBeanFieldGroup.class.getName()).
                        log(Level.FINE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(MBeanFieldGroup.class.getName()).
                        log(Level.SEVERE, null, ex);
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

    public interface FieldGroupListener<T> {

        public void onFieldGroupChange(MBeanFieldGroup<T> beanFieldGroup);

    }

    /**
      * EXPERIMENTAL: The cross field validation support is still experimental
     * and its API is likely to change.
    *
     * A validator executed against the edited bean. Developer can do any
     * validation within the validate method, but typically this type of
     * validation are used for e.g. cross field validation which is not possible
     * with BeanValidation support in Vaadin.
     *
     * @param <T> the bean type to be validated.
     *
     */
    public interface MValidator<T> extends Serializable {

        /**
         *
         * @param value the bean to be validated
         * @throws Validator.InvalidValueException
         */
        public void validate(T value) throws Validator.InvalidValueException;

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        setBeanModified(true);
        if (listener != null) {
            listener.onFieldGroupChange(this);
        }
    }

    private LinkedHashMap<MValidator<T>, Collection<String>> mValidators = new LinkedHashMap<MValidator<T>, Collection<String>>();

    /**
     * EXPERIMENTAL: The cross field validation support is still experimental
     * and its API is likely to change.
     * 
     * @param validator a validator that validates the whole bean making cross
     * field validation much simpler
     * @param properties the properties that this validator affects and on which
     * a possible error message is shown.
     * @return this FieldGroup
     */
    public MBeanFieldGroup<T> addValidator(MValidator<T> validator,
            String... properties) {
        mValidators.put(validator, Arrays.asList(properties));
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

    @Override
    public boolean isValid() {
        // clear all errors
        for (Field f : getFields()) {
            if (f instanceof AbstractComponent) {
                AbstractComponent abstractField = (AbstractComponent) f;
                abstractField.setComponentError(null);
            }
        }
        // first check standard property level validators
        final boolean propertiesValid = super.isValid();
        if (propertiesValid) {
            // then crossfield(/bean level) validators 
            for (MValidator<T> v : mValidators.keySet()) {
                try {
                    v.validate(getItemDataSource().getBean());
                } catch (Validator.InvalidValueException e) {
                    Collection<String> properties = mValidators.get(v);
                    for (String p : properties) {
                        Field<?> field = getField(p);
                        if (field instanceof AbstractComponent) {
                            AbstractComponent abstractField = (AbstractComponent) field;
                            abstractField.setComponentError(
                                    AbstractErrorMessage.
                                    getErrorMessageForException(e));
                        }
                    }
                    // TODO what to do if developer didn't specify any properties
                    // to which validato was bound to? Create a bean level
                    // error message that could be used by e.g. AbstractForm?
                    return false;
                }
            }
            return true;
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
     * group has been modified
     * @return the MBeanFieldGroup that can be used for further modifications or
     * e.g. commit if buffered
     */
    public MBeanFieldGroup<T> withEagerValidation(FieldGroupListener<T> listener) {
        this.listener = listener;
        for (Field<?> field : getFields()) {
            ((AbstractComponent) field).setImmediate(true);
            field.addValueChangeListener(this);
            if (field instanceof MTextField) {
                final MTextField abstractTextField = (MTextField) field;
                abstractTextField.setEagerValidation(true);
            }
            // TODO DRY, create interface eagervalidateable, or just push Vaadin
            // core team to get this done
            if (field instanceof MPasswordField) {
                final MPasswordField abstractPwField = (MPasswordField) field;
                abstractPwField.setEagerValidation(true);
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

    }

}
