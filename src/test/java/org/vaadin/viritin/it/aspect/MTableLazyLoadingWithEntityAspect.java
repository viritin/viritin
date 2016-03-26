package org.vaadin.viritin.it.aspect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanComparator;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.testdomain.Service;
import org.vaadin.viritin.testdomain.User;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;

/**
 * Test reading default methods
 * Code borrowed from MTableLazyLoadingWithSorting
 * @author Klaus Sausen
 */
@Theme("valo")
public class MTableLazyLoadingWithEntityAspect extends AbstractTest {

	private static final long serialVersionUID = 1L;

	@Override
	public Component getTestComponent() {

		final List<User> listOfPersons = 
				Service
				.getListOfPersons(1000).stream()
				.map(person -> new User(person))
				.collect(Collectors.toList());
		
		MTable<User> table = new MTable<>(
				(firstRow, sortAscending, property) -> {
					if (property != null) {
						Collections.sort(listOfPersons, new BeanComparator<User>(
								property));
						if (!sortAscending) {
							Collections.reverse(listOfPersons);
						}
					}
					int last = firstRow + LazyList.DEFAULT_PAGE_SIZE;
					if (last > listOfPersons.size()) {
						last = listOfPersons.size();
					}
					return new ArrayList<User>(listOfPersons.subList(firstRow,
							last));
				},
				() -> (int)Service.count()
				)
				.withProperties("localizedSalutation", "person.firstName", "person.lastName")
				.withColumnHeaders("Salutation", "Forename", "Name")
				.withFullWidth();

		return table;
	}

}
