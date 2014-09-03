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
import org.vaadin.maddon.fields.MTextField;

/**
 *
 * @author mattitahvonenitmill
 * @param <T> the type of the bean wrapped by this group
 */
public class MBeanFieldGroup<T> extends BeanFieldGroup<T> implements
        Property.ValueChangeListener, FieldEvents.TextChangeListener {

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
}
