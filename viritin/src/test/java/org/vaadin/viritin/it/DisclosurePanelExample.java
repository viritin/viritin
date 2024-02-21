package org.vaadin.viritin.it;

import org.vaadin.viritin.components.DisclosurePanel;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Component;

/**
 * An example how to extend
 *
 * @author Matti Tahvonen
 */
public class DisclosurePanelExample extends AbstractTest {

    private static final long serialVersionUID = 2638100034569162593L;

    @Override
    public Component getTestComponent() {
        
        RichText c = new RichText().withMarkDown("This is the content");

        DisclosurePanel d = new DisclosurePanel("Expert Mode", c)
                .setClosedIcon(VaadinIcons.CARET_RIGHT)
                .setOpenIcon(VaadinIcons.CARET_DOWN);

        return new MVerticalLayout(d);

    }

}
