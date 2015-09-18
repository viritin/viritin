
package org.vaadin.viritin;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
public class MGridTest {
    
    @Test
    public void testTypedSelection() {
        
        List<Person> listOfPersons = Service.getListOfPersons(100);
        
        MGrid<Person> g = new MGrid<Person>();
        g.setRows(listOfPersons);
        
        final Person selectedDude = listOfPersons.get(2);

        // Awesome, now the API actually tells you what you should pass as 
        // parameters and what you'll get as return type.
        g.selectRow(selectedDude);
        Person selectedRow = g.getSelectedRow();
        
        Assert.assertEquals(selectedDude, selectedRow);
        
    }

}
