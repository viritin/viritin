/*
 * Copyright 2014 Matti Tahvonen.
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

package org.vaadin.viritin.testdomain;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.ObjectUtils;

/**
 *
 * @author mattitahvonenitmill
 */
public class Person {
    private int id;

    @NotNull
    @Size(min = 3, max = 15)
    private String firstName;
    private String lastName;
    
    @NotNull
    private Integer age;

    private List<Address> addresses = new ArrayList<Address>();

    private List<Group> groups = new ArrayList<Group>();

    public Person() {
    }

    public Person(int id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> adresses) {
        this.addresses = adresses;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + firstName + " "
                + lastName + ", age=" + age + ", addresses=" + addresses
                + ", groups=" + groups + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Person other = (Person) obj;
        if (ObjectUtils.notEqual(age, other.age))
            return false;
        if (firstName == null) {
            if (other.firstName != null)
                return false;
        } else if (!firstName.equals(other.firstName))
            return false;
        if (id != other.id)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + age;
        result = prime * result
                + ((firstName == null) ? 0 : firstName.hashCode());
        result = prime * result + id;
        result = prime * result
                + ((lastName == null) ? 0 : lastName.hashCode());
        return result;
    }

}
