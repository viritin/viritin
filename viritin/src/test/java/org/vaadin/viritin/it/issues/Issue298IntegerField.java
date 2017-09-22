/*
 * Copyright 2017 Matti Tahvonen.
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
package org.vaadin.viritin.it.issues;

import com.vaadin.data.Binder;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

import org.vaadin.viritin.fields.IntegerField;

public class Issue298IntegerField extends UI {

    @Override
    protected void init(VaadinRequest request) {
        Model model = new Model();
        model.setValue(0);

        this.value.addValueChangeListener((evt) -> System.out.println("value is: " + evt.getValue()));

        this.value.setValue(1);

        // expect to see 'value is: 1' here
        this.binder.bindInstanceFields(this);
        this.binder.setBean(model);

        // expect to see 'value is: 1' here
        this.value.setValue(2);

        // expect to see 'value is: 2' here but exception is thrown instead

        setContent(this.value);
    }

    static public class Model {

        private int value;

        public int getValue() {
            return this.value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private IntegerField value = new IntegerField("Model Value");
    private Binder<Model> binder = new Binder<>(Model.class);

    public Issue298IntegerField() {
    }

}
