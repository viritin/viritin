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
package org.vaadin.maddon.label;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * Label whose content is wrapped in an H1,H2,H3.. element.
 * 
 * Uses Jsoup for sanitation. Only text by default.
 */
public class Header extends Label {
    
    private String text;
    private int headerLevel = 1;
    private Whitelist whitelist = Whitelist.none();

    public Whitelist getWhitelist() {
        return whitelist;
    }

    public Header setWhitelist(Whitelist whitelist) {
        this.whitelist = whitelist;
        return this;
    }

    public String getText() {
        return text;
    }

    public Header setText(String text) {
        this.text = text;
        return this;
    }

    public int getHeaderLevel() {
        return headerLevel;
    }

    public Header setHeaderLevel(int headerLevel) {
        if(headerLevel < 1 || headerLevel > 6) {
            throw new IllegalArgumentException("Header levels 1-6 supported");
        }
        this.headerLevel = headerLevel;
        return this;
    }

    public Header(String headerText) {
        text = headerText;
    }

    @Override
    public void setValue(String newStringValue) {
        setText(newStringValue);
    }
    
    @Override
    public void beforeClientResponse(boolean initial) {
        setContentMode(ContentMode.HTML);
        StringBuilder sb = new StringBuilder("<h");
        sb.append(headerLevel);
        sb.append(">");
        sb.append(Jsoup.clean(text, whitelist));
        sb.append("</h");
        sb.append(headerLevel);
        sb.append(">");
        super.setValue(sb.toString());
        super.beforeClientResponse(initial);
    }
    
}
