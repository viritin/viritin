package org.vaadin.viritin.layouts;

import org.vaadin.viritin.fluency.ui.FluentPanel;

import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;

/**
 * Fluent Panel
 */
public class MPanel extends Panel implements FluentPanel<MPanel> {

    private static final long serialVersionUID = -7384406421724902867L;

    public MPanel() {
        super();
    }

    public MPanel(Component content) {
        super(content);
    }

    public MPanel(String caption) {
        super(caption);
    }

    public MPanel(String caption, Component content) {
        super(caption, content);
    }
}
