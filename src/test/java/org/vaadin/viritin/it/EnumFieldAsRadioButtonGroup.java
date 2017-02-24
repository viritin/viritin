package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.v7.data.util.ObjectProperty;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.OptionGroup;
import com.vaadin.ui.themes.ValoTheme;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.testdomain.Address.AddressType;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class EnumFieldAsRadioButtonGroup extends AbstractTest {

    private static final long serialVersionUID = -825607383677011983L;

    @Override
    public Component getTestComponent() {

        EnumSelect<AddressType> select = new EnumSelect<AddressType>()
                .withSelectType(OptionGroup.class);
        select.setStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

        // The Enum type is detected when the edited property is bound to select
        // This typically comes form basic bean binding, but here done manually.
        ObjectProperty objectProperty = new ObjectProperty(AddressType.Home);
        select.setPropertyDataSource(objectProperty);
        
        // Alternatively, if not using databinding at all, you could just use 
        // basic TypedSelect, or the method from it
        // select.setOptions(AddressType.values());

        return select;
    }

}
