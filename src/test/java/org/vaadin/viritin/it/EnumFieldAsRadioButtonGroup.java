package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.ui.OptionGroup;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.testdomain.Address.AddressType;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class EnumFieldAsRadioButtonGroup extends AbstractTest {
    
    @Override
    public Component getTestComponent() {
        
        EnumSelect select = new EnumSelect().withSelectType(OptionGroup.class);
        // This typically comes form bean binding
        ObjectProperty objectProperty = new ObjectProperty(AddressType.Home);
        select.setPropertyDataSource(objectProperty);
        
        return select;
    }
    
}
