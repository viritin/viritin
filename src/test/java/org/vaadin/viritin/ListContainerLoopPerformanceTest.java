package org.vaadin.viritin;

import org.vaadin.viritin.v7.ListContainer;
import com.vaadin.v7.data.Item;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.util.BeanItemContainer;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.output.NullOutputStream;
import org.junit.Test;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * Tests and compares performance of ListContainer when looping through all
 * items and reading all their properties. Not a real life scenario as typically
 * only a fraction of items in large containers are accessed. But still, 
 * the execution time shouldn't radically grow during development and shouldn't
 * be radically longer than with other in memory containers.
 * 
 * Running the test couple of times to "warm-up" JIT. Increase the loop count
 * if you want to see if JIT still affects more.
 * 
 * Results fastest times (update if these changes dramatically):
 * 
 * Version 1.35-SNAPSHOT: LC 148ms, BIC 160ms, 2.3Gz i7 1st gen retina macbook 
 * 
 *
 */
public class ListContainerLoopPerformanceTest {

    @Test
    public void runPerformanceTests() throws InterruptedException, IOException {
        for (int i = 0; i < 4; i++) {
            loopAllEntitiesAndPropertiesWithBeanItemContainer();
            System.gc();
            Thread.sleep(200);
            System.gc();
            Thread.sleep(200);
            loopAllEntitiesAndProperties();
            System.gc();
            Thread.sleep(200);
            System.gc();
            Thread.sleep(200);
        }
    }

    public void loopAllEntitiesAndProperties() throws IOException {
        NullOutputStream nullOutputStream = new NullOutputStream();
        List<Person> listOfPersons = Service.getListOfPersons(100 * 1000);
        long currentTimeMillis = System.currentTimeMillis();
        ListContainer<Person> listContainer = new ListContainer<>(
                listOfPersons);
        Collection<?> ids = listContainer.getContainerPropertyIds();
        for (int i = 0; i < listContainer.size(); i++) {
            Item item = listContainer.getItem(listOfPersons.get(i));
            for (Object propertyId : ids) {
                Property itemProperty = item.getItemProperty(propertyId);
                final Object value = itemProperty.getValue();
                nullOutputStream.write(value.toString().getBytes());
                LOG.log(Level.FINEST, "Property: %s", value);
            }
        }
        LOG.
                log(Level.INFO,
                        "Looping all properties in 100 000 Items took {0}ms",
                        (System.currentTimeMillis() - currentTimeMillis));
    }

    public void loopAllEntitiesAndPropertiesWithBeanItemContainer() throws IOException {
        NullOutputStream nullOutputStream = new NullOutputStream();

        List<Person> listOfPersons = Service.getListOfPersons(100 * 1000);
        long currentTimeMillis = System.currentTimeMillis();
        BeanItemContainer<Person> c = new BeanItemContainer<>(
                Person.class, listOfPersons);
        Collection<?> ids = c.getContainerPropertyIds();
        for (int i = 0; i < c.size(); i++) {
            Item item = c.getItem(listOfPersons.get(i));
            for (Object propertyId : ids) {
                Property itemProperty = item.getItemProperty(propertyId);
                final Object value = itemProperty.getValue();
                nullOutputStream.write(value.toString().getBytes());
                LOG.log(Level.FINEST, "Property: %s", value);
            }
        }
        // ~ 350ms in 1.34, MacBook Pro (Retina, Mid 2012) 2.3Gz i7
        // ~ + 3-10ms in 1.35, when changing ListContainer to use PropertyUtils instead of WrapDynaBean
        LOG.
                log(Level.INFO,
                        "BIC from core: Looping all properties in 100 000 Items took {0}ms",
                        (System.currentTimeMillis() - currentTimeMillis));
    }

    private static final Logger LOG = Logger.getLogger(
            ListContainerLoopPerformanceTest.class.getName());

}
