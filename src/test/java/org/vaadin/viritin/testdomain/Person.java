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

/**
 *
 * @author mattitahvonenitmill
 */
public class Person {
    private String firstName;
    private String lastName;
    private int age;
    
    private List<Address> addresses = new ArrayList<Address>();
    
    private List<Group> groups = new ArrayList<Group>();

    public Person() {
    }
    
    public Person(String firstName, String lastName, int age) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" + "firstName=" + firstName + ", lastName=" + lastName + ", age=" + age + ", adresses=" + addresses + ", groups=" + groups + '}';
    }


    
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Person person = (Person)o;

    if (age != person.age) {
      return false;
    }
    if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null) {
      return false;
    }
    if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = firstName != null ? firstName.hashCode() : 0;
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + age;
    return result;
  }
  
}
