package org.vaadin.maddon;

import org.vaadin.maddon.testdomain.Person;
import com.vaadin.data.Property;
import com.vaadin.ui.ListSelect;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Assert;
import org.junit.Test;
import org.vaadin.maddon.fields.CaptionGenerator;
import org.vaadin.maddon.fields.MValueChangeEvent;
import org.vaadin.maddon.fields.MValueChangeListener;
import org.vaadin.maddon.fields.TypedSelect;

/**
 *
 * @author matti ät vaadin.com
 */
public class TypedSelectUsage {
    
    public static final int MAYBE_TOO_MUCH_FOR_IT = new Date().getYear() - 80;
    MutableObject<Person> selectedValue = new MutableObject();
    Person me = new Person("Matti", "Tahvonen", MAYBE_TOO_MUCH_FOR_IT);

    @Test
    public void testTypedSelect() {
        // The universal Java API, pojos and a List
        List<Person> options = new ArrayList();
        options.add(me);
        // The other fellow
        options.add(new Person("Metrin", "Slerba", 69));
        
        // Core vaadin select usage for a warmup
        ListSelect listSelect = new ListSelect();
        // This is the easies method, if you have a toString method that is 
        // suitable for option text in your objects. If not, you should use a
        // built in or custom container with a suitable "property" to be configured
        // for caption usage
        listSelect.addItems(options);
        listSelect.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                // The value is "itemId" with the simpe addItems style. If you 
                // are using some "special container", it may be something else, 
                // you just have to know it ;-)
                Person value = (Person) event.getProperty().getValue();
            }
        });

        // Vaadin core selects also supports "multiselection" via same componet.
        // This is partly why the current API is "suboptimal".
        //listSelect.setMultiSelect(true);
        listSelect.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                // If it is in "multiselect mode", the value is a Set of persons
                // or again something else with custom containers
                Set<Person> value = (Set<Person>) event.getProperty().getValue();
            }
        });
        
        /** Actual TypedSelect usage */

        // Better typed alternative for Vaadin select
        final TypedSelect<Person> typedSelect = new TypedSelect<Person>();
        // Uses "NativeSelect" as impl. by default, but all core select types are 
        // supported. E.g.: withSelectType(ListSelect.class);

        // Typed API for setOptions makes its usage easier to figure out where are we 
        // selecting from. Note that its "fluent" so you can chain it directly into
        // constrcutor as well.
        typedSelect.setOptions(options);

        // for most objects you want to customize the caption text. Again, you
        // can use chaind invocation and you'll use lambdas in you java 8 project.
        typedSelect.setCaptionGenerator(new CaptionGenerator<Person>() {

            @Override
            public String getCaption(Person option) {
                return option.getFirstName() + " " + option.getLastName();
            }
        });

        typedSelect.addMValueChangeListener(new MValueChangeListener<Person>() {

            @Override
            public void valueChange(MValueChangeEvent<Person> event) {
                // The thing you actually want in your listener, the selected 
                // entity is now available directly via the event and, no 
                // casting required like with core.
                Person value = event.getValue();

                // assign the value for testing purposes
                selectedValue.setValue(value);
            }
        });
        
        // set value and ensure listener is called
        Assert.assertNotSame(me, selectedValue.getValue());
        typedSelect.setValue(me);
        Assert.assertSame(me, selectedValue.getValue());
        
    }

}
