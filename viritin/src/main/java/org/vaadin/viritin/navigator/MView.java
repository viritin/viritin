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
package org.vaadin.viritin.navigator;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;

/**
 * Interface for all views controlled by the {@code MNavigator}. 
 * Each view added to the navigator must implement this interface. 
 * Typically, a view is a {@link com.vaadin.ui.Component}.
 * 
 * <p>
 * This is a drop-in replacement for {@link com.vaadin.navigator.View}
 * which has been enhanced to know not only when a view is entered <i>into</i>
 * (the {@code enter()} method), but also when a view is <i>exiting</i>
 * (the {@code beforeViewChange()} and {@code afterViewChange()} methods).
 * 
 * <p>
 * This class can be used with both {@code MNavigator} and {@code Navigator}.
 * However, in order for the {@code beforeViewChange()} and {@code afterViewChange()}
 * methods to actually be invoked the view <i>must</i> be used with {@code MNavigator}.
 */
public interface MView extends View {

    /**
     * Invoked before the view is changed.
     * 
     * 
     * <p>
     * By returning {@code false} from this method the view change is 
     * denied. More information in {@link com.vaadin.navigator.ViewChangeListener#beforeViewChange(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)}.
     *
     * 
     * @param event view change event
     * @return true if the view change should be allowed or this listener does
     *    not care about the view change, false to block the change
     * 
     * @see com.vaadin.navigator.ViewChangeListener#beforeViewChange(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
     * 
     */
    public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event);
    
    /**
     * Invoked after the view is changed. If a {@code beforeViewChange} method blocked
     * the view change, this method is not called. Be careful of unbounded
     * recursion if you decide to change the view again in this class.
     *
     * <p> 
     * This is a logical place to deregister listeners that are owned
     * by the view.
     *
     * @param event view change event
     */
    public void afterViewChange(ViewChangeListener.ViewChangeEvent event);

    
}
