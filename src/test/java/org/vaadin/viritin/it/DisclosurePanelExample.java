package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.components.DisclosurePanel;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * An example how to extend
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class DisclosurePanelExample extends AbstractTest {

    private static final long serialVersionUID = 2638100034569162593L;

    @Override
    public Component getTestComponent() {
        
        RichText c = new RichText().withMarkDown("This is the content");

        DisclosurePanel d = new DisclosurePanel("Expert Mode", c)
        .setClosedIcon(FontAwesome.CARET_RIGHT)
        .setOpenIcon(FontAwesome.CARET_DOWN);

        return new MVerticalLayout(d);

    }

}
