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

import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;

/**
 * A navigator utility that allows switching of views in a part of an
 * application.
 *
 * <p>
 * This is a drop-in replacement for {@link com.vaadin.navigator.Navigator}. It
 * uses an enhanced {@code View} implementation, called {@link MView}. It is
 * still possible to use old-style {@code View}s with {@code MNavigator} but
 * then there is no gain to using {@code MNavigator} over {@code Navigator}.
 *
 *
 *
 */
public class MNavigator extends Navigator {

    private static final long serialVersionUID = 1438630556557157686L;

    /**
     * Creates a navigator that is tracking the active view using URI fragments
     * of the {@link com.vaadin.server.Page} containing the given UI and
     * replacing the contents of a {@link com.vaadin.ui.ComponentContainer} with
     * the active view.
     *
     * <p>
     * This constructor is exactly similar to
     * {@link com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI, com.vaadin.ui.ComponentContainer)}
     *
     * @param ui the UI for which the Navigator is to be created
     * @param container the component container used for views
     * @see com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI,
     * com.vaadin.ui.ComponentContainer)
     */
    public MNavigator(UI ui, ComponentContainer container) {
        super(ui, container);
    }

    /**
     * Creates a navigator.
     *
     * <p>
     * This constructor is exactly similar to
     * {@link com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI, com.vaadin.navigator.NavigationStateManager, com.vaadin.navigator.ViewDisplay)}
     *
     * @param ui the UI for which the Navigator is to be created
     * @param stateManager the NavigationStateManager to be used by this
     * Navigator
     * @param display the ViewDisplay that will be used for showing the views
     * @see com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI,
     * com.vaadin.navigator.NavigationStateManager,
     * com.vaadin.navigator.ViewDisplay)
     */
    public MNavigator(UI ui, NavigationStateManager stateManager, ViewDisplay display) {
        super(ui, stateManager, display);
    }

    /**
     * Creates a navigator that is tracking the active view using URI fragments
     * of the {@link com.vaadin.server.Page} containing the given UI and
     * replacing the contents of a
     * {@link com.vaadin.ui.SingleComponentContainer} with the active view.
     *
     * <p>
     * This constructor is exactly similar to
     * {@link com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI, com.vaadin.ui.SingleComponentContainer)}
     *
     * @param ui the UI for which the Navigator is to be created
     * @param container the component in which the views are to be displayed
     * @see com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI,
     * com.vaadin.ui.SingleComponentContainer)
     */
    public MNavigator(UI ui, SingleComponentContainer container) {
        super(ui, container);
    }

    /**
     * Creates a navigator that is tracking the active view using URI fragments
     * of the {@link com.vaadin.server.Page} containing the given UI.
     *
     * <p>
     * This constructor is exactly similar to
     * {@link com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI, com.vaadin.navigator.ViewDisplay)}
     *
     * @param ui the UI for which the Navigator is to be created
     * @param display the ViewDisplay that will be used for showing the views
     * @see com.vaadin.navigator.Navigator#Navigator(com.vaadin.ui.UI,
     * com.vaadin.navigator.ViewDisplay)
     */
    public MNavigator(UI ui, ViewDisplay display) {
        super(ui, display);
    }

    /**
     * Creates a navigator. This method is for use by dependency injection
     * frameworks etc. and must be followed by a call to
     * {@link #init(UI, NavigationStateManager, ViewDisplay) init()} before use.
     *
     * @since 7.6
     */
    protected MNavigator() {
    }

    /**
     * {@inheritDoc} Generally shouldn't be called directly.
     */
    @Override
    protected void init(UI ui, NavigationStateManager stateManager, ViewDisplay display) {
        super.init(ui, stateManager, display);
        addViewChangeListener(new MViewChangeListener());
    }

    // ViewChangeListener which actually does the work of propagating the
    // beforeViewChange and afterViewChange into the relevant views.
    // The listener exists for the life-time of the navigator. It never
    // gets unregistered.
    private static class MViewChangeListener implements ViewChangeListener {

        private static final long serialVersionUID = -3311491180251673472L;

        @Override
        public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {
            View oldView = event.getOldView();
            if (oldView != null && oldView instanceof MView) {
                return ((MView) oldView).beforeViewChange(event);
            }
            return true;
        }

        @Override
        public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {
            View oldView = event.getOldView();
            if (oldView != null && oldView instanceof MView) {
                ((MView) oldView).afterViewChange(event);
            }
        }
    }

}
