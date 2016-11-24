package org.vaadin.viritin.fluency.ui;

import org.vaadin.viritin.fluency.event.FluentFieldEvents.FluentBlurNotifier;
import org.vaadin.viritin.fluency.event.FluentFieldEvents.FluentFocusNotifier;

import com.vaadin.ui.Window;

/**
 * The base interface for fluent versions of {@link Window}
 * 
 * @author Daniel Nordhoff-Vergien
 * @see Window
 */
public interface IWindow extends FluentPanel<IWindow>,
        FluentFocusNotifier<IWindow>, FluentBlurNotifier<IWindow> {

}
