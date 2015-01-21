package org.vaadin.viritin.it;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.fields.ElementCollectionField;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.FilterableTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MultiSelectTable;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Address;
import org.vaadin.viritin.testdomain.Group;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

import java.util.List;

@Theme("valo")
public class FilterTablePerson extends AbstractTest {

    @Override
    public Component getTestComponent() {
        final FilterableTable<Person> table = new FilterableTable<Person>();
        table.withProperties("firstName", "lastName", "age");
        table.setBeans(Service.getListOfPersons(200));

        final MTextField search = new MTextField();
        search.setInputPrompt("Filter on last name...");
        search.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                table.removeAllFilters();
                if(StringUtils.isNotEmpty(event.getText()))  {
                    table.addFilter(new SimpleStringFilter("lastName",
                            event.getText(), true, true));
                }
            }
        });

        return new MVerticalLayout()
                        .withFullWidth()
                .add(search)
                        .expand(table);
    }

}
