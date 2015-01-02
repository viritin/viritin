
package org.vaadin.maddon.it;

import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.maddon.fields.MultiSelectTable;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.maddon.testdomain.Group;
import org.vaadin.maddon.testdomain.Person;
import org.vaadin.maddon.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
public class EditPerson extends AbstractTest {
    
    public static class PersonForm<Person> extends AbstractForm {
        
        private final MultiSelectTable<Group> groups = new MultiSelectTable<Group>()
                .withProperties("name")
                .setOptions(Service.getAvailableGroups())
                ;

        @Override
        protected Component createContent() {
            return new MVerticalLayout(groups, getToolbar());
        }
        
    }

    @Override
    public Component getTestComponent() {
        PersonForm form = new PersonForm();
        
        Person p = Service.getPerson();
        form.setEntity(p);
        
        form.setSavedHandler(new AbstractForm.SavedHandler<Person>() {

            @Override
            public void onSave(Person entity) {
                Notification.show(entity.toString());
            }
        });
        
        return form;
    }

}
