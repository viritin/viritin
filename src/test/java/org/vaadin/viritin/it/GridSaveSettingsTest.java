package org.vaadin.viritin.it;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class GridSaveSettingsTest extends AbstractTest {

	@Override
	public Component getTestComponent() {
		final VerticalLayout layout = new VerticalLayout();
		MGrid table=new MGrid();
		BeanItemContainer<Person> ds = new BeanItemContainer<Person>(
				Person.class);
		layout.setMargin(true);
		layout.setSpacing(true);
		Label info =new Label();
		info.setValue("Hide and/or sort columns."
				+ " Grid will save your settings in the browser cookies."
				+ " And load them next time you open the view");
		ds.addBean(new Person(1, "Some", "One",13));
		ds.addBean(new Person(2, "Arni", "Red",83));
		ds.addBean(new Person(3, "Sami", "Green",42));
		ds.addBean(new Person(4, "Togy", "Tilly",23));
		ds.addBean(new Person(5, "Billy", "Willy",22));
		ds.addBean(new Person(6, "Anna", "Bunny",23));
		ds.addBean(new Person(7, "Joe", "Black",52));
		table = new MGrid();
		table.setContainerDataSource(ds);
		makeGridColumnsHideable(table);
		makeGridOrderable(table);
		table.attachSaveSettings("personsGrid");
		layout.addComponent(info);
		layout.addComponent(table);
		
		return layout;
	}
	private void makeGridOrderable(MGrid table) {
		table.setColumnReorderingAllowed(true);
		
	}
	private void makeGridColumnsHideable(Grid grid) {
		grid.getColumns().forEach(c->{
			c.setHidable(true);
		});
	}
}
