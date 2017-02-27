package org.vaadin.viritin.v7;

import com.vaadin.annotations.Theme;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.LazyComboBoxUsage;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.v7.fields.CaptionGenerator;
import org.vaadin.viritin.v7.fields.LazyComboBox;
import org.vaadin.viritin.v7.fields.MValueChangeEvent;
import org.vaadin.viritin.v7.fields.MValueChangeListener;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Person;

import java.util.List;
import org.vaadin.viritin.v7.fields.IconGenerator;

@Theme("valo")
public class LazyComboBoxUsageSample extends AbstractTest {

    @Override
    public Component getTestComponent() {

        final LazyComboBoxUsage.LazyService service = new LazyComboBoxUsage.LazyService();

        // This is naturally much cleaner with Java 8, just wire to service layer
        // using method reference, with older java, better create a class for 
        // each entity type.
        final LazyComboBox.FilterablePagingProvider filterablePagingProvider = new LazyComboBox.FilterablePagingProvider() {

            @Override
            public List findEntities(int firstRow, String filter) {
                System.err.
                        println("find entities " + firstRow + " f: " + filter);
                return service.findPersons(filter, firstRow,
                        LazyList.DEFAULT_PAGE_SIZE);
            }
        };
        final LazyComboBox.FilterableCountProvider filterableCountProvider = new LazyComboBox.FilterableCountProvider() {

            @Override
            public int size(String filter) {
                System.err.println("size " + filter);
                return service.countPersons(filter);
            }

        };

        final LazyComboBox<Person> cb = new LazyComboBox(Person.class,
                filterablePagingProvider, filterableCountProvider)
                .setCaptionGenerator(new CaptionGenerator<Person>() {

                    private static final long serialVersionUID = 5611804984518152177L;

                    @Override
                    public String getCaption(Person option) {
                        return option.getFirstName() + " " + option.
                                getLastName();
                    }
                })
                .setIconGenerator(new IconGenerator<Person>() {
                    private static final long serialVersionUID = -795330075732173785L;

                    @Override
                    public Resource getIcon(Person option) {
                        return FontAwesome.AMBULANCE;
                    }
                });
        
        // Use following to verify ComboBox don't need to loop the whole DB
        // This shouldn't cause extra queries to backend, even with 7.5
        Person selection = service.findPersons("99", 0, 1).get(0);
        cb.setValue(selection);

        cb.addMValueChangeListener(new MValueChangeListener<Person>() {

            private static final long serialVersionUID = -115227386482631657L;

            @Override
            public void valueChange(MValueChangeEvent<Person> event) {
                Notification.show("Selected value :" + event.getValue());
            }
        });

        Button toggle = new Button("Toggle readonly");
        toggle.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                cb.setReadOnly(!cb.isReadOnly());
            }
        });

        return new MVerticalLayout(cb, toggle);
    }

}
