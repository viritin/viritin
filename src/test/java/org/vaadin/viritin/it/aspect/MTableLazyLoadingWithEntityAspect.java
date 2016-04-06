/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Random;

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
        
        Random r = new Random(0);

		final List<User> listOfPersons = 
				Service
				.getListOfPersons(1000).stream()
				.map(person -> {
                    User u = new User(person);
                    u.setLocale(r.nextBoolean() ? Locale.ENGLISH : Locale.FRENCH);
                    return u;
                })
				.collect(Collectors.toList());
		
		MTable<User> table = new MTable<>(
				(firstRow, sortAscending, property) -> {
					if (property != null) {
						Collections.sort(listOfPersons, new BeanComparator<>(
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
				.withProperties("localizedSalutation","locale", "person.firstName", "person.lastName")
				.withColumnHeaders("Salutation", "Locale", "Forename", "Name")
				.withFullWidth()
                .withGeneratedColumn("localizedSalutation", u-> MessageFormat.format(
                        u.getLocalizedSalutation(), u.getPerson().getFirstName()));

		return table;
	}

}
