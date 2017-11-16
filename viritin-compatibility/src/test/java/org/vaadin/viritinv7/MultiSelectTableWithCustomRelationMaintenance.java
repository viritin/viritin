package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.util.Set;

import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritinv7.fields.LabelField;
import org.vaadin.viritinv7.fields.MTextField;
import org.vaadin.viritinv7.fields.MultiSelectTable;
import org.vaadin.viritinv7.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Group;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 *
 * @author Matti Tahvonen
 */
@Theme("valo")
public class MultiSelectTableWithCustomRelationMaintenance extends AbstractTest {

    private static final long serialVersionUID = -2682172644856319883L;

    public static class PersonForm extends AbstractForm<Person> {

        private static final long serialVersionUID = 4144805837285124445L;

        private final MTextField firstName = new MTextField("Name");

        private final MTextField age = new MTextField("Age");

        LabelField<Integer> id = new LabelField<>(Integer.class)
                .withCaption("ID");
        
        private final MultiSelectTable<Group> groups = new MultiSelectTable<Group>(){
            private static final long serialVersionUID = 1229334178491680961L;
            @Override
            protected void addRelation(Set<Group> newValues) {
                super.addRelation(newValues);

                /*
                 * One could do custom maintenance here for each Group instances 
                 */
                for (Group g : newValues) {
                    System.out.println("Adding group " + g);
                }
                
            }

            @Override
            protected void removeRelation(Set<Group> orphaned) {
                super.removeRelation(orphaned);
                /*
                 * One could do custom maintenance here for each Group instances 
                 */
                for (Group g : orphaned) {
                    System.out.println("Removing group " + g);
                }
            }
        
        }.
                withProperties("name")
                .setOptions(Service.getAvailableGroups());

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
        final PersonForm form = new PersonForm();

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

                System.out.println("\nAdded relations:");
                System.out.println(form.groups.getAllAddedRelations());
                System.out.println("\nRemoved relations:");
                System.out.println(form.groups.getAllRemovedRelations());
                System.out.println("\nAll modified relations:");
                System.out.println(form.groups.getAllModifiedRelations());
                
                form.groups.clearModifiedRelations();
                
            }
        });

        form.setDeleteHandler(new AbstractForm.DeleteHandler<Person>() {
            private static final long serialVersionUID = -6298152846013943120L;

            @Override
            public void onDelete(Person entity) {
                Notification.show("Delete: " + entity.toString());
            }
        });

        Button openInPopup = new Button("Open in popup");
        openInPopup.addClickListener(new Button.ClickListener() {
            private static final long serialVersionUID = 5019806363620874205L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                final PersonForm form = new PersonForm();

                Person p = Service.getPerson();
                form.setEntity(p);

                form.setSavedHandler(new AbstractForm.SavedHandler<Person>() {
                    private static final long serialVersionUID = 1008970415395369248L;

                    @Override
                    public void onSave(Person entity) {
                        Notification.show(entity.toString());
                    }
                });

                form.setDeleteHandler(new AbstractForm.DeleteHandler<Person>() {
                    private static final long serialVersionUID = -6298152846013943120L;

                    @Override
                    public void onDelete(Person entity) {
                        Notification.show("Delete: " + entity.toString());
                    }
                });
                form.setResetHandler(new AbstractForm.ResetHandler<Person>() {
                    private static final long serialVersionUID = -1695108652595021734L;

                    @Override
                    public void onReset(Person entity) {
                        Notification.show("Nothing done");
                        form.getPopup().close();
                    }
                });
                form.openInModalPopup();

            }
        });

        return new MVerticalLayout(form, openInPopup);
    }

}
