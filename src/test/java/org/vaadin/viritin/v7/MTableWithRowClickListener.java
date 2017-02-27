package org.vaadin.viritin.v7;

import com.vaadin.annotations.Theme;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.TextField;
import org.vaadin.addonhelpers.AbstractTest;
import org.vaadin.viritin.v7.MBeanFieldGroup;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.NestedPropertyTest;
import org.vaadin.viritin.v7.fields.MTable;
import org.vaadin.viritin.v7.fields.MTextField;
import org.vaadin.viritin.v7.form.AbstractForm;

@Theme("valo")
public class MTableWithRowClickListener extends AbstractTest {

    public static class Form extends AbstractForm<NestedPropertyTest.Entity> {

        private static final long serialVersionUID = -8683293957823897252L;
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
            final MBeanFieldGroup<NestedPropertyTest.Entity> mbfg = super.
                    bindEntity(entity);
            mbfg.bind(nestedPropertyField, "detail.property");
            return mbfg;
        }

    }

    @Override
    public Component getTestComponent() {
        final MTable<NestedPropertyTest.Entity> table = new MTable(
                NestedPropertyTest.getEntities(3))
                .withProperties(
                        "id",
                        "property",
                        "detail.property", // "property" field from object in "detail" field
                        "numbers[2]", // third object from collection in field "integers"
                        "stringToInteger(foo)", // Integer with key "foo" from map stringToInteger
                        "detailList[1].detail2.property",
                        "detailList[1].moreDetails[1].property"
                )
                .withHeight("300px")
                .expandFirstColumn();
        table.refreshRowCache();
        
        final Form form = new Form();
        form.setSavedHandler(
                new AbstractForm.SavedHandler<NestedPropertyTest.Entity>() {

                    private static final long serialVersionUID = 5841730427136575503L;

                    @Override
                    public void onSave(NestedPropertyTest.Entity entity) {
                        table.refreshRowCache();
                    }
                });

        table.addRowClickListener(
                new MTable.RowClickListener<NestedPropertyTest.Entity>() {

                    private static final long serialVersionUID = -5651811812313380992L;

                    @Override
                    public void rowClick(
                            MTable.RowClickEvent<NestedPropertyTest.Entity> event) {
                                form.setEntity(event.getEntity());
                                table.setValue(event.getEntity());
                            }
                });

        return new MVerticalLayout(
                table, form);
    }

}
