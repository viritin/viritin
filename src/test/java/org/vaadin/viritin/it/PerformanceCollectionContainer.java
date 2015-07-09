package org.vaadin.viritin.it;

import java.util.Arrays;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.data.collectioncontainer.CollectionContainer;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class PerformanceCollectionContainer extends AbstractTest {
    @Override
    public Component getTestComponent() {
        CollectionContainer collectionContainer = new CollectionContainer(PerformanceListContainer.LIST_OF_PERSONS, true, CollectionContainer.ITEM_ID_MODE_OBJECT);
        Table table = new Table();
        table.setContainerDataSource(collectionContainer, Arrays.asList("firstName", "lastName"));
		return table;
    }
    
}
