package org.vaadin.viritin.v7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.NestedPropertyTest;
import org.vaadin.viritin.v7.grid.MGrid;

@Theme("valo")
public class MGridWithNestedProperty extends AbstractTest {

    @Override
    public Component getTestComponent() {
        final MGrid<NestedPropertyTest.Entity> grid = new MGrid(NestedPropertyTest.getEntities(3))
                .withProperties(
                        "id",
                        "property",
                        "detail.property", // "property" field from object in "detail" field
                        "numbers[2]", // third object from collection in field "integers"
                        "stringToInteger(foo)", // Integer with key "foo" from map stringToInteger
                        "detailList[1].detail2.property",
                        "detailList[1].moreDetails[1].property"
                )
                .withHeight("300px");
        return new MVerticalLayout(grid);
    }

}
