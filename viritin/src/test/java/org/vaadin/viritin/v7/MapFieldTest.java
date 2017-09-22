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
package org.vaadin.viritin.v7;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.util.HashMap;
import java.util.Map;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.v7.BeanBinder;
import org.vaadin.viritin.v7.fields.MapField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author mattitahvonenitmill
 */
public class MapFieldTest extends AbstractTest {

    public static class TestClass {

        private Map<String, Double> mapField = new HashMap<>();

        private Map<String, Integer> stringToInt = new HashMap<>();

        private Map<String, Integer> stringToString = new HashMap<>();

        public Map<String, Double> getMapField() {
            return mapField;
        }

        public void setMapField(Map<String, Double> mapField) {
            this.mapField = mapField;
        }

        public Map<String, Integer> getStringToInt() {
            return stringToInt;
        }

        public void setStringToInt(Map<String, Integer> stringToInt) {
            this.stringToInt = stringToInt;
        }

        public Map<String, Integer> getStringToString() {
            return stringToString;
        }

        public void setStringToString(Map<String, Integer> stringToString) {
            this.stringToString = stringToString;
        }

        @Override
        public String toString() {
            return "TestClass{" + "mapField=" + mapField + ", stringToInt=" + stringToInt + ", stringToString=" + stringToString + '}';
        }
                

    }

    MapField mapField = new MapField();
    MapField stringToInt = new MapField();
    MapField stringToString = new MapField();

    @Override
    public Component getTestComponent() {
        
        mapField.setCaption("String->Double");
        stringToInt.setCaption("Strint->Integer");
        stringToString.setCaption("Strint->String");
        
        TestClass bean = new TestClass();
        
        bean.getMapField().put("first", 1.0);

        BeanBinder.bind(bean, this);

        Button button = new Button("Show value", e -> {
            Notification.show(bean.toString());
        });
        
        return new MVerticalLayout(mapField, stringToInt, stringToString, button);

    }

}
