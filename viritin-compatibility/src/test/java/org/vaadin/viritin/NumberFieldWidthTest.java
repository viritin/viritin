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
package org.vaadin.viritin;

import com.vaadin.server.Sizeable;
import com.vaadin.v7.ui.TextField;
import org.junit.Assert;
import org.junit.Test;
import org.vaadin.viritinv7.fields.IntegerField;

/**
 *
 * @author mattitahvonenitmill
 */
public class NumberFieldWidthTest {

    TextField underlayingField;

    @Test
    public void testSizeDefined() {

        IntegerField integerField = new IntegerField() {
            private static final long serialVersionUID = -2915829267538851760L;

            @Override
            public void setWidth(float width, Sizeable.Unit unit) {
                super.setWidth(width, unit);
                underlayingField = tf;
            }
            
        };
        
        Assert.assertEquals(-1, integerField.getWidth(),0.001);
        
        integerField.setWidth("99%");
        
        Assert.assertEquals(99, integerField.getWidth(),0.001);
        Assert.assertEquals(100, underlayingField.getWidth(),0.001);

        integerField.setSizeUndefined();;

        Assert.assertEquals(-1, integerField.getWidth(),0.001);
        Assert.assertEquals(-1, underlayingField.getWidth(),0.001);
        
    }
}
