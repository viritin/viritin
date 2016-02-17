
package org.vaadin.viritin.it;

import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.EmailField;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author Matti Tahvonen
 */
public class EmailFieldExample extends AbstractTest {

    @Override
    public Component getTestComponent() {
        return new EmailField();
    }

}
