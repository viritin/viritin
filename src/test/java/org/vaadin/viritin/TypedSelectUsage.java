package org.vaadin.viritin;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TwinColSelect;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Assert;
import org.junit.Test;
import org.vaadin.viritin.fields.CaptionGenerator;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.fields.config.ComboBoxConfig;
import org.vaadin.viritin.testdomain.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author matti ät vaadin.com
 */
public class TypedSelectUsage {

    public static final int MAYBE_TOO_MUCH_FOR_IT = new Date().getYear() - 80;
    MutableObject<Person> selectedValue = new MutableObject();
    Person me = new Person(0, "Matti", "Tahvonen", MAYBE_TOO_MUCH_FOR_IT);

    private List<Person> getOptions() {
        // The universal Java API, pojos and a List
        List<Person> options = new ArrayList();
        options.add(me);
        // The other fellow
        options.add(new Person(0, "Metrin", "Slerba", 69));
        return options;
    }

    private MValueChangeListener<Person> getValueChangeListener() {
        return new MValueChangeListener<Person>() {

            @Override
            public void valueChange(MValueChangeEvent<Person> event) {
                // The thing you actually want in your listener, the selected
                // entity is now available directly via the event and, no
                // casting required like with core.
                Person value = event.getValue();

                // assign the value for testing purposes
                selectedValue.setValue(value);
            }
        };
    }

    @Test
    public void testTypedSelect() {
        // Core vaadin select usage for a warmup
        ListSelect listSelect = new ListSelect();
        // This is the easies method, if you have a toString method that is
        // suitable for option text in your objects. If not, you should use a
        // built in or custom container with a suitable "property" to be configured
        // for caption usage
        listSelect.addItems(getOptions());
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
        typedSelect.setOptions(getOptions());

        // for most objects you want to customize the caption text. Again, you
        // can use chaind invocation and you'll use lambdas in you java 8 project.
        typedSelect.setCaptionGenerator(new CaptionGenerator<Person>() {

            @Override
            public String getCaption(Person option) {
                return option.getFirstName() + " " + option.getLastName();
            }
        });

        typedSelect.addMValueChangeListener(getValueChangeListener());

        // set value and ensure listener is called
        Assert.assertNotSame(me, selectedValue.getValue());
        typedSelect.setValue(me);
        Assert.assertSame(me, selectedValue.getValue());
    }


    @Test
    public void testTypedSelectWithConfiguredComboBox() {
        // Core vaadin select usage for a warmup
        ComboBox comboBox = new ComboBox();
        comboBox.addItems(getOptions());
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.setPageLength(10);
        comboBox.setTextInputAllowed(true);
        comboBox.addItems(getOptions());

        // other configuration's are similar to testTypedSelect...

        /** Actual TypedSelect usage */
        final TypedSelect<Person> typedSelect = new TypedSelect<Person>();
        // TypedSelect only provides configurations that are similar to all select-types (AbstractSelect)
        // for example the ComboBox has some options that other AbstractSelect don't have
        // by using asComboBoxType instead of typedSelect.withSelectType(ComboBox.class)
        // you can perform individual configurations
        typedSelect.asComboBoxType(ComboBoxConfig.build()
                .withFilteringMode(FilteringMode.CONTAINS)
                .withPageLength(10)
                .withTextInputAllowed(true));
        // it's important that you configure the type of TypedSelect before assigning the options!
        typedSelect.setOptions(getOptions());

        typedSelect.addMValueChangeListener(getValueChangeListener());

        Assert.assertNotSame(me, selectedValue.getValue());
        typedSelect.setValue(me);
        Assert.assertSame(me, selectedValue.getValue());
    }

}
