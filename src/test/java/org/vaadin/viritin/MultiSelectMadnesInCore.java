package org.vaadin.viritin;

import org.vaadin.viritin.v7.ListContainer;
import com.vaadin.v7.data.util.converter.Converter;
import com.vaadin.ui.Component;
import com.vaadin.v7.ui.Table;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;
import org.vaadin.viritin.v7.fields.MultiSelectTable;
import org.vaadin.viritin.v7.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.testdomain.Group;
import org.vaadin.viritin.testdomain.Person;
import org.vaadin.viritin.testdomain.Service;

/**
 * A test and use case for MultiSelectTable
 *
 * @author Matti Tahvonen
 *
 */
public class MultiSelectMadnesInCore {

    public static class PersonFormWithCoreTable extends AbstractForm {

        private static final long serialVersionUID = -4766609148902175465L;
        private Table groups = new Table();

        public PersonFormWithCoreTable() {
            setEagerValidation(true);
            groups.setMultiSelect(true);
            groups.setContainerDataSource(new ListContainer<>(Service.
                    getAvailableGroups()));
            Class c = groups.getType();

            // The presentation type of multiselect table is Set, but you 
            // cannot give such converter for it :-(
            // And, Vaadin is doing weirdish doublecounversion when pushing data
            // to data sources, so it is probably not possible to implement a
            // "modifying collection container". So this is not, and not even
            // used in the example....
            Converter<Object, Collection> modifyingCollectionConverter = new Converter() {

                @Override
                public Object convertToModel(Object value, Class targetType,
                        Locale locale) throws Converter.ConversionException {
                    Collection originalCollection;
                    if (groups.getPropertyDataSource().getValue() != null) {
                        originalCollection = (Collection) groups.
                                getPropertyDataSource().getValue();
                    } else {
                        // create collection with correct type supports List/Set
                        Class c = groups.getPropertyDataSource().getType();
                        if (c.isInterface()) {
                            if (c == List.class) {
                                originalCollection = new ArrayList();
                            } else {
                                // assume set, try to use HashSet
                                originalCollection = new HashSet();
                            }
                        } else {
                            try {
                                originalCollection = (Collection) c.
                                        newInstance();
                            } catch (IllegalAccessException | InstantiationException ex) {
                                throw new RuntimeException(
                                        "Unsupported collection type", ex);
                            }
                        }
                        groups.getPropertyDataSource().setValue(
                                originalCollection);
                    }
                    Collection c = (Collection) groups.getValue();
                    Set orphaned = new HashSet(c);
                    for (Object o : c) {
                        orphaned.remove(o);
                        if (!originalCollection.contains(o)) {
                            originalCollection.add(o);
                        }
                    }
                    originalCollection.removeAll(orphaned);
                    return originalCollection;
                }

                @Override
                public Object convertToPresentation(Object value,
                        Class targetType, Locale locale) throws Converter.ConversionException {
                    // Vaadin does some really hackish double conversions when 
                    // pushing back the data to entity :-( this will never work
                    // properly...
                    Set s = new HashSet();
                    if (value != null) {
                        s.addAll((Collection) value);
                    }
                    return s;
                }

                @Override
                public Class getModelType() {
                    return Collection.class;
                }

                @Override
                public Class getPresentationType() {
                    return Set.class;
                }
            };
//            groups.setConverter(modifyingCollectionConverter);

            Converter<Object, Collection> nonModifyingCollectionConverter = new Converter() {

                @Override
                public Object convertToModel(Object value, Class targetType,
                        Locale locale) throws Converter.ConversionException {
                    Set set = (Set) value;
                    // create collection with correct type supports List/Set
                    Class c = groups.getPropertyDataSource().getType();
                    if (c.isInterface()) {
                        if (c == List.class) {
                            return new ArrayList(set);
                        } else {
                            // assume set, try to use HashSet
                            return new HashSet(set);
                        }
                    } else {
                        try {
                            Collection col = (Collection) c.
                                    newInstance();
                            col.addAll(set);
                            return col;
                        } catch (IllegalAccessException | InstantiationException ex) {
                            throw new RuntimeException(
                                    "Unsupported collection type",
                                    ex);
                        }
                    }
                }

                @Override
                public Object convertToPresentation(Object value,
                        Class targetType, Locale locale) throws Converter.ConversionException {
                    // Vaadin does some really hackish double conversions when 
                    // pushing back the data to entity :-( this will never work
                    // properly...
                    Set s = new HashSet();
                    if (value != null) {
                        s.addAll((Collection) value);
                    }
                    return s;
                }

                @Override
                public Class
                        getModelType() {
                    return Collection.class;
                }

                @Override
                public Class
                        getPresentationType() {
                    return Set.class;
                }
            };
            groups.setConverter(nonModifyingCollectionConverter);

        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(groups);
        }

    }

    public static class PersonForm extends AbstractForm<Person> {

        private static final long serialVersionUID = 6410815033625409608L;
        MultiSelectTable<Group> groups = new MultiSelectTable()
                .setOptions(Service.getAvailableGroups());

        {
            setEagerValidation(true);
        }

        @Override
        protected Component createContent() {
            return new MVerticalLayout(groups);
        }
    }

    @Test
    public void bindMultiSelectFormWithCoreComponent() {

        PersonFormWithCoreTable f = new PersonFormWithCoreTable();
        final Person person = Service.getPerson();
        List<Group> originalList = person.getGroups();

        f.setEntity(person);

        // FIXME avoid odd cast
        Object o = f.groups.getValue();
        Set value = (Set) o;
        Assert.assertEquals(1, value.size());
        Assert.assertEquals(person.getGroups().get(0), value.iterator().next());

        f.groups.select(Service.getAvailableGroups().get(2));
        f.groups.select(Service.getAvailableGroups().get(3));

        Assert.assertEquals(3, person.getGroups().size());

        o = f.groups.getValue();
        value = (Set) o;

        Assert.assertEquals(3, value.size());

        // FIXME typing
        for (Object v : value) {
            Assert.assertTrue(person.getGroups().contains(v));
        }

        // TODO it should be possible to keep the list same/write final for the 
        // groups collection in Person∫
        //Assert.assertSame(originalList, person.getGroups());
    }

    @Test
    public void bindMultiSelectForm() {

        PersonForm f = new PersonForm();
        final Person person = Service.getPerson();
        List<Group> originalList = person.getGroups();

        f.setEntity(person);

        // No odd casts, the table value is always Collection of your referenced types
        Collection<Group> value = f.groups.getValue();
        Assert.assertEquals(1, value.size());
        Assert.assertEquals(person.getGroups().get(0), value.iterator().next());

        f.groups.select(Service.getAvailableGroups().get(2));
        f.groups.select(Service.getAvailableGroups().get(3));

        Assert.assertEquals(3, person.getGroups().size());

        value = f.groups.getValue();

        Assert.assertEquals(3, value.size());

        for (Group v : value) {
            Assert.assertTrue(person.getGroups().contains(v));
        }

        // The backed list is kept the same
        Assert.assertSame(originalList, person.getGroups());
        
    }

}
