package org.vaadin.viritinv7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.TextField;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritinv7.MBeanFieldGroup;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.NestedPropertyTest;
import org.vaadin.viritinv7.fields.MTable;
import org.vaadin.viritinv7.fields.MTextField;
import org.vaadin.viritinv7.fields.MValueChangeEvent;
import org.vaadin.viritinv7.fields.MValueChangeListener;
import org.vaadin.viritinv7.form.AbstractForm;

@Theme("valo")
public class MTableWithNestedProperty extends AbstractTest {

    public static class Form extends AbstractForm<NestedPropertyTest.Entity> {

        private static final long serialVersionUID = -3164966078873765005L;
        private final TextField property = new MTextField("property");
        // Appears not to work, core issue!? @PropertyId("detail.property")
        private final TextField nestedPropertyField = new MTextField("detail.property");

        @Override
        protected Component createContent() {
            return new MVerticalLayout(property, nestedPropertyField,
                    getToolbar());
        }

        @Override
        protected MBeanFieldGroup<NestedPropertyTest.Entity> bindEntity(
                NestedPropertyTest.Entity entity) {
            final MBeanFieldGroup<NestedPropertyTest.Entity> mbfg = super.bindEntity(entity);
            mbfg.bind(nestedPropertyField, "detail.property");
            return mbfg;
        }
        
        
    }

    @Override
    public Component getTestComponent() {
        final MTable<NestedPropertyTest.Entity> table = new MTable(NestedPropertyTest.getEntities(3))
                .withProperties(
                        "id",
                        "property",
                        "detail.property", // "property" field from object in "detail" field
                        "numbers[2]", // third object from collection in field "integers"
                        "stringToInteger(foo)", // Integer with key "foo" from map stringToInteger
                        "detailList[1].detail2.property",
                        "detailList[1].moreDetails[1].property"
                )
                .withHeight("300px");
        final Form form = new Form();
        form.setSavedHandler(
                new AbstractForm.SavedHandler<NestedPropertyTest.Entity>() {

                    private static final long serialVersionUID = 5142651230686909799L;

                    @Override
            public void onSave(NestedPropertyTest.Entity entity) {
                table.refreshRowCache();
            }
        });
        
        table.addMValueChangeListener(new MValueChangeListener<NestedPropertyTest.Entity>() {

            private static final long serialVersionUID = -2918207986940924806L;

            @Override
            public void valueChange(
                    MValueChangeEvent<NestedPropertyTest.Entity> event) {
                if(event.getValue() != null) {
                    form.setEntity(event.getValue());
                }
            }
        });

        return new MVerticalLayout(
                table, form);
    }

}
