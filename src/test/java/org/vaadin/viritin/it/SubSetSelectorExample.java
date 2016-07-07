package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.CaptionGenerator;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.SubSetSelector;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * Subset selector is a component that is designed to pick an handful of objects
 * from a larger set of option.
 * 
 * @author Matti Tahvonen
 */
@Theme("valo")
public class SubSetSelectorExample extends AbstractTest {
    

    @Override
    public Component getTestComponent() {
        SubSetSelector<Person> sss = new SubSetSelector<Person>(Person.class) {
            @Override
            protected Person instantiateOption(String stringInput) {
                Person person = new Person();
                if(stringInput != null) {
                    String[] split = stringInput.split(" ", 2);
                    person.setFirstName(split[0]);
                    if(split.length > 1) {
                        person.setLastName(split[1]);
                    }
                }
                // required field, but not used in form, in this test case
                person.setAge(69);
                return person;
            }
        };

        List<Person> available = Service.getListOfPersons(100);
        
        CaptionGenerator<Person> cg = option -> option.getFirstName() + " " + option.getLastName();

        // New Items can be added with combobox text if needed, String goes to instantiateOption method
        sss.setNewItemsAllowed(true);

        // This is optional and can replace setNewItemsAllowed as well (then new items using + button)
        sss.setNewInstanceForm(new AbstractForm<Person>() {

            MTextField firstName = new MTextField("First name");
            MTextField lastName = new MTextField("Last name");

            @Override
            protected Component createContent() {
                return new MFormLayout(firstName, lastName, getToolbar());
            }

            @Override
            protected void adjustSaveButtonState() {
                // Always enabled as just adding new ones and option may be valid with combobox input alone
            }
        });

        sss.setNewInstanceForm(null);

        sss.setCaptionGenerator(cg);
        
        sss.setVisibleProperties("firstName", "lastName", "age");
        sss.setOptions(available);
        
        Set<Person> selected = new HashSet<>();
        
        selected.add(available.get(3));
        
        sss.setValue(selected);
        
        sss.addValueChangeListener((Property.ValueChangeEvent event) -> {
            Notification.show("Value now :" + sss.getValue());
        });
        
        
        Button setNull = new Button("setValue(null)");
        setNull.addClickListener(e->{sss.setValue(null);});
        
        Button setList = new Button("setValue(new ArrayList())");
        setList.addClickListener(e->{sss.setValue(new ArrayList());});

        return new MVerticalLayout(sss, setNull, setList);
        
    }


}
