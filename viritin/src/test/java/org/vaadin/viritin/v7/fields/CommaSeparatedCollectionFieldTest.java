package org.vaadin.viritin.v7.fields;

import org.vaadin.viritin.v7.fields.CommaSeparatedCollectionField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.*;

public class CommaSeparatedCollectionFieldTest {

    private CommaSeparatedCollectionField fieldUnderTest;

    @Before
    public void setUp() throws Exception {
        fieldUnderTest = new CommaSeparatedCollectionField();
    }

    @Test
    public void basicStringCollection() {
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("Foo", "Bar"));

        fieldUnderTest.setValue(strings);

        String textFieldPresentation = fieldUnderTest.textField.getValue();

        assertEquals("Foo, Bar", textFieldPresentation);

        // simulate user value change event
        fieldUnderTest.textField.userValueChange = true;
        fieldUnderTest.textField.setValue("Foo, Bar,Car");
        fieldUnderTest.textField.userValueChange = false;

        Collection value = fieldUnderTest.getValue();
        assertEquals(3, value.size());

    }

    public static class Test1 {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Test1{" + "value=" + value + '}';
        }

        public static Test1 valueOf(String str) {
            Test1 test1 = new Test1();
            test1.setValue(str);
            return test1;
        }
    }

    @Test
    public void testWithValueOfFuction() {
        ArrayList list = new ArrayList();

        CommaSeparatedCollectionField field = new CommaSeparatedCollectionField();
        field.setElementType(Test1.class);
        field.setValue(list);

        field.textField.userValueChange = true;
        field.textField.setValue("Foo");
        field.textField.userValueChange = false;

        Test1 get = (Test1) list.get(0);
        assertEquals("Foo", get.getValue());

        field.textField.userValueChange = true;
        field.textField.setValue("Foo, bar");
        field.textField.userValueChange = false;

        get = (Test1) list.get(1);
        assertEquals("bar", get.getValue());
        
        list.clear();
        list.add(16);
        field.setElementType(Integer.class);
        field.setValue(null);
        field.setValue(list);
        
        assertEquals("16", field.textField.getValue());
        
        field.textField.userValueChange = true;
        field.textField.setValue("2, 1");
        field.textField.userValueChange = false;

        Integer int1 =  (Integer) list.get(1);
        assertEquals(Integer.valueOf(1), int1);

    }

    public static class Test2 {

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Test1{" + "value=" + value + '}';
        }

        public Test2(String value) {
            this.value = value;
        }

    }

    @Test
    public void testWithConstructor() {
        ArrayList list = new ArrayList();

        CommaSeparatedCollectionField field = new CommaSeparatedCollectionField();
        field.setElementType(Test2.class);
        field.setValue(list);

        field.textField.userValueChange = true;
        field.textField.setValue("Foo");
        field.textField.userValueChange = false;

        Test2 get = (Test2) list.get(0);
        assertEquals("Foo", get.getValue());

        field.textField.userValueChange = true;
        field.textField.setValue("Foo, bar");
        field.textField.userValueChange = false;

        get = (Test2) list.get(1);
        assertEquals("bar", get.getValue());

    }

}
