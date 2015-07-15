package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.NestedPropertyTest;
import org.vaadin.viritin.fields.MTable;

@Theme("valo")
public class MTableWithNestedProperty extends AbstractTest {
    
    @Override
    public Component getTestComponent() {
        
        return new MVerticalLayout(
                new MTable(NestedPropertyTest.getEntities(3)).withProperties(
                        "id",
                        "property",
                        "numbers[2]", // third object from collection in field "integers"
                        "detail.property", // "property" field from object in "detail" field 
                        "detailList[1].detail2.property",
                        "detailList[1].moreDetails[1].property"
                ));
    }
    
}
