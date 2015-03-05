package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.CaptionGenerator;
import org.vaadin.viritin.fields.CheckBoxGroup;
import org.vaadin.viritin.fields.LabelField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Group;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class CheckBoxGroupTest extends AbstractTest {
    
    public static class PersonForm extends AbstractForm<Person> {
        
        private MTextField firstName = new MTextField("Name");
        
        private MTextField age = new MTextField("Age");
        
        private LabelField<Integer> id = new LabelField<Integer>(Integer.class)
                .withCaption("ID");
        
        private final CheckBoxGroup<Group> groups = new CheckBoxGroup<Group>()
                .setOptions(Service.getAvailableGroups())
                .setCaptionGenerator(new CaptionGenerator<Group>() {
                    
                    @Override
                    public String getCaption(Group option) {
                        return option.getName();
                    }
                });

        public PersonForm() {
        }
        
        @Override
        protected Component createContent() {
            return new MVerticalLayout(id, firstName, age, groups,
                    getToolbar());
        }
        
    }
    
    @Override
    public Component getTestComponent() {
        PersonForm form = new PersonForm();
        
        form.addValidityChangedListener(
                new AbstractForm.ValidityChangedListener<Person>() {
                    @Override
                    public void onValidityChanged(
                            AbstractForm.ValidityChangedEvent<Person> event) {
                                if (event.getComponent().isValid()) {
                                    Notification.show("The form is now valid!",
                                            Notification.Type.TRAY_NOTIFICATION);
                                } else {
                                    Notification.show(
                                            "Invalid values in form, clicking save is disabled!");
                                }
                            }
                });
        
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
