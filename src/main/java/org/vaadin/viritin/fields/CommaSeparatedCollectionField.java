/*
 * Copyright 2016 Matti Tahvonen.
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

import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * A simple TextField based component to edit collections of objects, which can
 * be converted to String presentation and back.
 * <p>
 * Both split and joinSeparator can be specified and one can provide custom
 * strategies to convert from String presentation to element types.
 */
public class CommaSeparatedCollectionField extends CustomField<Collection> {

    public interface FromStringInstantiator<T> {

        T instance(String stringPresentation);
    }

    MTextField textField = new MTextField();
    private Collection collection;
    private String splitSeparatorRegexp = "\\s*,\\s*";
    private String joinSeparator = ", ";
    private Class<?> elementType;
    private FromStringInstantiator instantiator;

    public CommaSeparatedCollectionField() {
        textField.addValueChangeListener(new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (textField.isUserValueChange()) {

                    String[] parts = textField.getValue().split(splitSeparatorRegexp);
                    // TODO null check for collection and create it if not set (e.g. null in property)
                    collection.clear();

                    for (final String part : parts) {
                        if (instantiator != null) {
                            collection.add(instantiator.instance(part));
                        } else if (elementType != null) {

                            try {

                                Method declaredMethod = elementType.getDeclaredMethod("valueOf", String.class);
                                collection.add(declaredMethod.invoke(null, part));
                            } catch (NoSuchMethodException ex) {
                                try {
                                    collection.add(elementType.getConstructor(String.class).newInstance(part));
                                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex1) {
                                throw new RuntimeException("The string " + part + " could not be converted to " + elementType.getSimpleName(), ex1);
                                }
                            } catch (IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException ex) {
                                throw new RuntimeException("The string " + part + " could not be converted to " + elementType.getSimpleName(), ex);
                            }
                        } else {
                            // assume String values in the collection
                            collection.add(part);
                        }
                    }
                }
                fireValueChange(true);
            }
        });
    }

    @Override
    protected Component initContent() {
        return textField;
    }

    @Override
    public Class<? extends Collection> getType() {
        return Collection.class;
    }

    @Override
    protected void setInternalValue(Collection newValue) {
        this.collection = newValue;

        String toPresentation = StringUtils.join(collection, joinSeparator);
        textField.setValue(toPresentation);
        super.setInternalValue(newValue);
    }

    public Class<?> getElementType() {
        return elementType;
    }

    /**
     * Sets the type of element in the collection. The class needs to have
     * either static valueOf(String) method or a constructor with String
     * parameter. Those will be used to instantiate objects from user input.
     * Alternatively you can specify an explicit instantiator.
     * <p>
     * If the element type in collection is String, no elmentType or
     * instantiator is needed.
     *
     * @param elementType the type of objects in the collection.
     */
    public void setElementType(Class<?> elementType) {
        this.elementType = elementType;
    }

    public void setInstantiator(FromStringInstantiator instantiator) {
        this.instantiator = instantiator;
    }

    public FromStringInstantiator getInstantiator() {
        return instantiator;
    }

    public String getJoinSeparator() {
        return joinSeparator;
    }

    /**
     * @param joinSeparator the separator string to be used when joining objects
     * to string presentation for editing. The default value is ", "
     */
    public void setJoinSeparator(String joinSeparator) {
        this.joinSeparator = joinSeparator;
    }

    public String getSplitSeparatorRegexp() {
        return splitSeparatorRegexp;
    }

    /**
     * Sets the split separator that is used to slice the input string into a
     * parts. The value is a regular expression so you can strip away extra
     * spaces. The default value is "\\s*,\\s*".
     *
     * @param splitSeparatorRegexp the split separator
     */
    public void setSplitSeparatorRegexp(String splitSeparatorRegexp) {
        this.splitSeparatorRegexp = splitSeparatorRegexp;
    }

}
