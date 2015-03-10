package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.beanutils.BeanComparator;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class SortingWithCollections extends AbstractTest {

    @Override
    public Component getTestComponent() {
        final List<Person> listOfPersons = Service.getListOfPersons(100);
        
        final MTable<Person> table = new MTable(listOfPersons);
        
        table.addSortListener(new MTable.SortListener() {

            @Override
            public void onSort(final MTable.SortEvent event) {
                // Skip default Vaadin proprietary container level sorting
                event.preventContainerSort();
                // Use std JDK API
                Collections.sort(listOfPersons, new Comparator<Person>(){

                    @Override
                    public int compare(Person o1, Person o2) {
                        String sortProperty = event.getSortProperty();
                        /**
                         * You could do anything you want here. Here just
                         * use BeanComparator from commons-beanutils
                         */
                        BeanComparator<Person> beanComparator = new BeanComparator<Person>();
                        beanComparator.setProperty(sortProperty);
                        if(event.isSortAscending()) {
                            return beanComparator.compare(o1, o2);
                        } else {
                            return - beanComparator.compare(o1, o2);
                        }
                    }
                });
                // Notify Table that its content has changed
                table.refreshRowCache();
            }
        });

        return table;
    }

}
