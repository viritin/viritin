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
package org.vaadin.viritin.fields;

import com.vaadin.ui.TextField;
import org.vaadin.viritin.fluency.ui.FluentTextField;
import org.vaadin.viritin.util.HtmlElementPropertySetter;

/**
 *
 * @author mstahv
 */
public class MTextField extends TextField implements FluentTextField<MTextField> {

    private AutoComplete autocomplete;
    private AutoCapitalize autocapitalize;
    private AutoCorrect autocorrect;
    private Boolean spellcheck;

    public MTextField() {
    }

    public MTextField(String caption) {
        super(caption);
    }

    public MTextField(String caption, String value) {
        super(caption, value);
    }

    public MTextField(ValueChangeListener<String> valueChangeListener) {
        super(valueChangeListener);
    }

    public MTextField(String caption, ValueChangeListener<String> valueChangeListener) {
        super(caption, valueChangeListener);
    }

    public MTextField(String caption, String value, ValueChangeListener<String> valueChangeListener) {
        super(caption, value, valueChangeListener);
    }

    public void setSpellcheck(Boolean spellcheck) {
        this.spellcheck = spellcheck;
    }

    public Boolean getSpellcheck() {
        return spellcheck;
    }

    public MTextField withSpellCheckOff() {
        setSpellcheck(false);
        return this;
    }

    public enum Spellcheck {
        on, off
    }

    public enum AutoComplete {
        on, off
    }

    public enum AutoCorrect {
        on, off
    }

    public enum AutoCapitalize {
        on, off
    }
    
        public MTextField withAutocompleteOff() {
        return setAutocomplete(AutoComplete.off);
    }

    public MTextField setAutocomplete(AutoComplete autocomplete) {
        this.autocomplete = autocomplete;
        return this;
    }

    public AutoComplete getAutocomplete() {
        return autocomplete;
    }

    public MTextField withAutoCapitalizeOff() {
        return setAutoCapitalize(AutoCapitalize.off);
    }

    public MTextField setAutoCapitalize(AutoCapitalize autoCapitalize) {
        this.autocapitalize = autoCapitalize;
        return this;
    }

    public AutoCapitalize getAutoCapitalize() {
        return autocapitalize;
    }

    public MTextField withAutoCorrectOff() {
        return setAutoCorrect(AutoCorrect.off);
    }

    public MTextField setAutoCorrect(AutoCorrect autoCorrect) {
        this.autocorrect = autoCorrect;
        return this;
    }

    public AutoCorrect getAutoCorrect() {
        return autocorrect;
    }

    private HtmlElementPropertySetter heps;

    protected HtmlElementPropertySetter getHtmlElementPropertySetter() {
        if (heps == null) {
            heps = new HtmlElementPropertySetter(this);
        }
        return heps;
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
        if (initial) {
            if(spellcheck != null) {
                getHtmlElementPropertySetter().setProperty(
                        "spellcheck", spellcheck);
            }
            if (autocomplete != null) {
                // sending here to keep value if toggling visibility
                getHtmlElementPropertySetter().setProperty("autocomplete",
                        autocomplete.toString());
            }
            if (autocorrect != null) {
                // sending here to keep value if toggling visibility
                getHtmlElementPropertySetter().setProperty("autocorrect",
                        autocorrect.toString());
            }
            if (autocapitalize != null) {
                // sending here to keep value if toggling visibility
                getHtmlElementPropertySetter().setProperty("autocapitalize",
                        autocapitalize.toString());
            }
        }
    }


}
