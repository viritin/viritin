package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;

import org.vaadin.viritinv7.fields.CheckBoxGroup;
import org.vaadin.viritinv7.fields.LabelField;
import org.vaadin.viritinv7.fields.MTextField;
import org.vaadin.viritinv7.form.AbstractForm;
import org.vaadin.viritin.fields.CaptionGenerator;
import org.vaadin.viritin.it.AbstractTest;
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

    private static final long serialVersionUID = 9030446735101247343L;
    
    public static class PersonForm extends AbstractForm<Person> {

        private static final long serialVersionUID = 663449825951264623L;
        
        private MTextField firstName = new MTextField("Name");
        
        private MTextField age = new MTextField("Age");
        
        private LabelField<Integer> id = new LabelField<>(Integer.class)
                .withCaption("ID");
        
        private final CheckBoxGroup<Group> groups = new CheckBoxGroup<Group>()
                .setOptions(Service.getAvailableGroups())
                .setCaptionGenerator(new CaptionGenerator<Group>() {
                    private static final long serialVersionUID = -7476461236123873375L;
                    
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
                    private static final long serialVersionUID = -3688956596748617476L;
                    
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
            private static final long serialVersionUID = 1008970415395369248L;
            
            @Override
            public void onSave(Person entity) {
                Notification.show(entity.toString());
            }
        });
        
        return form;
    }
    
}
