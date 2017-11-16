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
package org.vaadin.viritinv7;

import com.vaadin.v7.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritinv7.BeanBinder;
import org.vaadin.viritinv7.fields.CommaSeparatedCollectionField;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class CommaSeparatedCollectionFieldUsage extends AbstractTest {

    public static class Pojo {

        private Set<String> tags = new HashSet<>();

        public Set<String> getTags() {
            return tags;
        }

        public void setTags(Set<String> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Pojo{" + "tags=" + tags + '}';
        }

    }

    CommaSeparatedCollectionField tags = new CommaSeparatedCollectionField();

    @Override
    public Component getTestComponent() {

        CommaSeparatedCollectionField f = new CommaSeparatedCollectionField();
        f.setCaption("Integers");
        ArrayList<Integer> ints = new ArrayList<>();
        f.setElementType(Integer.class);
        f.setValue(ints);
        f.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show(ints.toString());
            }
        });

        CommaSeparatedCollectionField f2 = new CommaSeparatedCollectionField();
        f2.setCaption("Set of strings, with | as separator");
        f2.setJoinSeparator(" | ");
        f2.setSplitSeparatorRegexp("\\s*|\\s*");
        Set<String> strs = new HashSet<>(Arrays.asList("foo", "bar", "car"));
        f2.setValue(strs);
        f2.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Notification.show(strs.toString());
            }
        });
        
        tags.setCaption("Bound editor");
        Pojo pojo = new Pojo();
        BeanBinder.bind(pojo, this);
        
        Button button = new Button("Show value", e -> {
            Notification.show(pojo.toString());
        });

        return new MVerticalLayout(f, f2, tags, button);
    }

}
