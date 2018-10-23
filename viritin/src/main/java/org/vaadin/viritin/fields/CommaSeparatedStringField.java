/*
 * Copyright 2018 Matti Tahvonen.
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

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mstahv
 */
public class CommaSeparatedStringField<CT extends Collection<String>> extends CustomField<CT> {

    private TextField tf = new TextField();
    private CT collection;

    public CommaSeparatedStringField() {
        tf.addValueChangeListener(e -> {
            if (e.isUserOriginated()) {
                List<String> set = new ArrayList<>();
                for (String s : tf.getValue().split(",\\s*")) {
                    set.add(s);
                }
                collection.removeAll(collection);
                collection.addAll(set);
                fireEvent(new ValueChangeEvent(CommaSeparatedStringField.this, collection, true));
            }
        });
        tf.setWidth("100%");
        setWidth("300px");
    }

    public CommaSeparatedStringField(String caption) {
        this();
        setCaption(caption);
    }

    @Override
    protected Component initContent() {
        return tf;
    }
    
    @Override
    protected void doSetValue(CT value) {
        this.collection = value;
        tf.setValue(value.toString()
                        .replaceAll("\\[", "")
                        .replaceAll("]", "")
        );
    }

    @Override
    public CT getValue() {
        return collection;
    }


}
