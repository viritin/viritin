
package org.vaadin.viritin.v7;

import org.vaadin.viritin.it.*;
import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.v7.fields.EmailField;
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

    @Override
    public String getDescription() {
        return "V7 compatibility EmailField test";
    }
    
    

}
