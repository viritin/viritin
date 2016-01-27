package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.OptionGroup;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.testdomain.Address.AddressType;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class TypedSelectOptionGroup extends AbstractTest {
    
    @Override
    public Component getTestComponent() {
        TypedSelect<AddressType> types = new TypedSelect<AddressType>()
                .withSelectType(OptionGroup.class)
                .setOptions(AddressType.values()); // AddressType is an enum
        types.selectFirst();
        return types;
    }

}
