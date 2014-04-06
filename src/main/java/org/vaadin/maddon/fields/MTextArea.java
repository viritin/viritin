/*
 * Copyright 2014 mattitahvonenitmill.
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

package org.vaadin.maddon.fields;

import com.vaadin.data.Property;
import com.vaadin.ui.TextArea;

/**
 *
 * @author mattitahvonenitmill
 */
public class MTextArea extends TextArea {

    public MTextArea() {
        configureMaddonStuff();
    }

    private void configureMaddonStuff() {
        setNullRepresentation("");
        setWidth("100%");
    }

    public MTextArea(String caption) {
        super(caption);
        configureMaddonStuff();
    }

    public MTextArea(Property dataSource) {
        super(dataSource);
        configureMaddonStuff();
    }

    public MTextArea(String caption, Property dataSource) {
        super(caption, dataSource);
        configureMaddonStuff();
    }

    public MTextArea(String caption, String value) {
        super(caption, value);
    }

    public MTextArea withRows(int rows) {
    	setRows(rows);
    	return this;
    }
    
    public MTextArea withWordwrap(boolean wordwrap) {
    	setWordwrap(wordwrap);
    	return this;
    }
    
    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        super.addValueChangeListener(listener);
        // Remove once 7.2 is out
        setImmediate(true);
    }
    
}
