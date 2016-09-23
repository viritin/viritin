package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import java.util.List;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MTableLazyLoadingEmptyList extends AbstractTest {
    
    @Override
    public Component getTestComponent() {
        
        final List<Person> backingList = Service.getListOfPersons(0);
        
        MTable<Person> mTable = new MTable<>(
                new LazyList.PagingProvider<Person>() {
                    
                    @Override
                    public List<Person> findEntities(int firstRow) {
                        return backingList.subList(firstRow, 0);
                    }
                },
                new LazyList.CountProvider() {
                    
                    @Override
                    public int size() {
                        return 0;
                    }
                }
        ).withProperties("firstName", "lastName", "email");

        mTable.withFullWidth().withHeight("300px");
        
        // Alternative way, with explicitly defined bean type
//        mTable = new MTable<Person>(Person.class).lazyLoadFrom(
//                new LazyList.PagingProvider<Person>() {
//                    
//                    @Override
//                    public List<Person> findEntities(int firstRow) {
//                        return backingList.subList(firstRow, 0);
//                    }
//                },
//                new LazyList.CountProvider() {
//                    
//                    @Override
//                    public int size() {
//                        return 0;
//                    }
//                }
//        );
        
        return mTable;
    }
    
}
