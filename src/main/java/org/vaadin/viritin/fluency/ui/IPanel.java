package org.vaadin.viritin.fluency.ui;

import org.vaadin.viritin.fluency.event.FluentAction.FluentNotifier;
import org.vaadin.viritin.fluency.server.FluentScrollable;

import com.vaadin.ui.Panel;

/**
 * The base interface for fluent versions of {@link Panel}
 * 
 * @author Daniel Nordhoff-Vergien
 * @see Panel
 */
public interface IPanel extends FluentScrollable<IPanel>,
        FluentComponent.FluentFocusable<IPanel>, FluentNotifier<IPanel>,
        FluentComponent<IPanel>, FluentSingleComponentContainer<IPanel> {

}
