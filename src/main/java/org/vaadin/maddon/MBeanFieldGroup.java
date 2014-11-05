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
package org.vaadin.maddon;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.vaadin.maddon.fields.MTextField;

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
                java.lang.reflect.Field declaredField = findDeclaredFiled(
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
                        log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(MBeanFieldGroup.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }

    protected java.lang.reflect.Field findDeclaredFiled(Object property,
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
                return findDeclaredFiled(property, clazz.getSuperclass());
            }
        }
    }

    public interface FieldGroupListener<T> {

        public void onFieldGroupChange(MBeanFieldGroup<T> beanFieldGroup);

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        setBeanModified(true);
        if (listener != null) {
            listener.onFieldGroupChange(this);
        }
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

    public MBeanFieldGroup<T> withEagarValidation() {
        return withEagarValidation(new FieldGroupListener() {
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
    public MBeanFieldGroup<T> withEagarValidation(FieldGroupListener<T> listener) {
        this.listener = listener;
        for (Field<?> field : getFields()) {
            ((AbstractComponent) field).setImmediate(true);
            field.addValueChangeListener(this);
            if (field instanceof MTextField) {
                final MTextField abstractTextField = (MTextField) field;
                abstractTextField.setEagerValidation(true);
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
            if (field instanceof MTextField) {
                final MTextField abstractTextField = (MTextField) field;
                abstractTextField.setEagerValidation(true);
                abstractTextField.removeTextChangeListener(this);
            }
            unbind(field);
        }

    }

}
