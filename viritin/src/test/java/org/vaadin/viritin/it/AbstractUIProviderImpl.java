/*
 * Copyright 2012 Vaadin Community.
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
package org.vaadin.viritin.it;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

/**
 *
 */
public class AbstractUIProviderImpl extends UIProvider {

    Class<? extends UI> dirtyHack;
    
    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
        String name = event.getRequest().getPathInfo();
        if(name == null) {
            return dirtyHack;
        }
        if(name.startsWith("/")) {
            name = name.substring(1);
        }
        if(name.contains("/")) {
            name = name.substring(0,name.indexOf("/"));
        }
        if (!"".equals(name) && !name.contains(".ico") && name.matches("[A-Za-z0-9.]*")) {
            try {
                String className = name;
                Class<? extends UI> forName = (Class<? extends UI>) Class.forName(className);
                if (forName != null) {
                    dirtyHack = forName;
                    return forName;
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        dirtyHack = TListUi.class;
        return TListUi.class;
    }
    
}