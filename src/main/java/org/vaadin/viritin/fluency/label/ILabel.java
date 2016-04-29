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
package org.vaadin.viritin.fluency.label;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Label;
import org.vaadin.viritin.fluency.data.FluentProperty;
import org.vaadin.viritin.fluency.ui.FluentAbstractComponent;

/**
 * The base interface for fluent versions of {@link Label}
 * 
 * @author Max Schuster
 * @see Label
 */
public interface ILabel extends FluentAbstractComponent<ILabel>,
        FluentProperty<ILabel, String>, FluentProperty.FluentViewer<ILabel>,
        FluentProperty.FluentValueChangeNotifier<ILabel> {

    // Javadoc copied form Vaadin Framework
    /**
     * Sets the converter used to convert the label value to the property data
     * source type. The converter must have a presentation type of String.
     *
     * @param converter The new converter to use.
     * @return this (for method chaining)
     */
    public ILabel withConverter(Converter<String, ?> converter);

}
