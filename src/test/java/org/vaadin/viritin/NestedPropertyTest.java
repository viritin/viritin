/*
 * Copyright 2015 Matti Tahvonen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.viritin;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Matti Tahvonen
 */
public class NestedPropertyTest {

    public static class Entity {

        static long counter = 0;

        private final Long id = counter++;
        private String property = "foo";
        private Detail detail = new Detail();
        private List<Long> numbers = Arrays.asList(1 * counter, 2 * counter,
                3 * counter);
        private List<Detail> detailList = Arrays.asList(new Detail(),
                new Detail());

        private Map<String, Integer> stringToInteger = new HashMap<String, Integer>();

        public Entity() {
            stringToInteger.put("id", id.intValue());
            stringToInteger.put("foo", 69);
        }

        public List<Detail> getDetailList() {
            return detailList;
        }

        public void setDetailList(List<Detail> detailList) {
            this.detailList = detailList;
        }

        public Map<String, Integer> getStringToInteger() {
            return stringToInteger;
        }

        public void setStringToInteger(Map<String, Integer> stringToInteger) {
            this.stringToInteger = stringToInteger;
        }

        public Long getId() {
            return id;
        }

        public List<Long> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<Long> numbers) {
            this.numbers = numbers;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public Detail getDetail() {
            return detail;
        }

        public void setDetail(Detail detail) {
            this.detail = detail;
        }

    }

    public static class Detail {

        private String property = "bar";

        private Detail2 detail2 = new Detail2();

        private List<Detail2> moreDetails = Arrays.asList(new Detail2(),
                new Detail2());
        private List nonTypedDetails = Arrays.asList(new Detail2(),
                new Detail2());

        public List getNonTypedDetails() {
            return nonTypedDetails;
        }

        public void setNonTypedDetails(List nonTypedDetails) {
            this.nonTypedDetails = nonTypedDetails;
        }

        public Detail2 getDetail2() {
            return detail2;
        }

        public void setDetail2(Detail2 detail2) {
            this.detail2 = detail2;
        }

        public List<Detail2> getMoreDetails() {
            return moreDetails;
        }

        public void setMoreDetails(List<Detail2> moreDetails) {
            this.moreDetails = moreDetails;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

    }

    public static class Detail2 {

        private Integer property = 69;

        public Integer getProperty() {
            return property;
        }

        public void setProperty(Integer property) {
            this.property = property;
        }

    }

    public NestedPropertyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDynaBeanItem() {

        DynaBeanItem<Entity> dynaBeanItem = new DynaBeanItem<Entity>(
                new Entity());

        Property itemProperty = dynaBeanItem.getItemProperty("property");

        Assert.assertNotNull(itemProperty);
        Assert.assertEquals("foo", itemProperty.getValue());

        Property nestedProperty = dynaBeanItem.
                getItemProperty("detail.property");
        Assert.assertNotNull(nestedProperty);
        Assert.assertEquals("bar", nestedProperty.getValue());
        Assert.assertEquals(String.class, nestedProperty.getType());

    }

    @Test
    public void testListContainer() {

        ListContainer<Entity> listContainer = getTestListContainer();

        Entity entity = listContainer.getIdByIndex(0);
        Item dynaBeanItem = listContainer.getItem(entity);
        Property itemProperty = dynaBeanItem.getItemProperty("property");

        Assert.assertNotNull(itemProperty);
        Assert.assertEquals("foo", itemProperty.getValue());

        // Should not be reported by default...
        boolean contains = listContainer.getContainerPropertyIds().contains(
                "detail.property");
        Assert.assertFalse(contains);

        // But should be found if explicitly requested
        Property nestedProperty = dynaBeanItem.
                getItemProperty("detail.property");
        Assert.assertNotNull(nestedProperty);
        Assert.assertEquals("bar", nestedProperty.getValue());

        Assert.assertEquals(String.class, nestedProperty.getType());

        Property indexedProperty = dynaBeanItem.
                getItemProperty("numbers[2]");
        Assert.assertNotNull(indexedProperty);
        Assert.assertEquals((entity.getId() + 1) * 3l, indexedProperty.
                getValue());
        Assert.assertEquals(Long.class, indexedProperty.getType());

        Property mappedProperty = dynaBeanItem.
                getItemProperty("stringToInteger(id)");
        Assert.assertNotNull(mappedProperty);
        Assert.
                assertEquals(entity.getId().intValue(), mappedProperty.
                        getValue());
        Assert.assertEquals(Integer.class, mappedProperty.getType());

        Property thirdLevel = dynaBeanItem.
                getItemProperty("detailList[1].property");
        Assert.assertNotNull(thirdLevel);
        Assert.assertEquals("bar", thirdLevel.getValue());
        Assert.assertEquals(String.class, thirdLevel.getType());

        Property awkward = dynaBeanItem.
                getItemProperty("detailList[1].moreDetails[0]");
        Assert.assertNotNull(awkward);
        Detail2 value = (Detail2) awkward.getValue();
        Assert.assertEquals(Integer.valueOf(69), value.getProperty());
        Assert.assertEquals(Detail2.class, awkward.getType());

        Property weird = dynaBeanItem.
                getItemProperty("detailList[1].detail2.property");
        Assert.assertNotNull(weird);
        Assert.assertEquals(69, weird.getValue());
        Assert.assertEquals(Integer.class, weird.getType());

        Property weirdest = dynaBeanItem.
                getItemProperty("detailList[1].moreDetails[1].property");
        Assert.assertNotNull(weirdest);
        Assert.assertEquals(69, weirdest.getValue());
        Assert.assertEquals(Integer.class, weirdest.getType());

        Property nonTypedListItem = dynaBeanItem.
                getItemProperty("detailList[1].nonTypedDetails[0]");
        Assert.assertNotNull(weirdest);
        final Detail2 value1 = (Detail2) nonTypedListItem.getValue();

        // Is of type Detail2, but reported type should be Object
        // (can't be inspected)
        Assert.assertEquals(Integer.valueOf(69), value1.getProperty());
        Assert.assertEquals(Object.class, nonTypedListItem.getType());
    }

    public static ListContainer<Entity> getTestListContainer() {
        ListContainer<Entity> listContainer = new ListContainer<Entity>(
                getEntities(3));
        return listContainer;
    }

    public static List<Entity> getEntities(int n) {
        ArrayList<Entity> arrayList = new ArrayList<Entity>();
        for (int i = 0; i < n; i++) {
            arrayList.add(new Entity());
        }
        return arrayList;
    }

}
