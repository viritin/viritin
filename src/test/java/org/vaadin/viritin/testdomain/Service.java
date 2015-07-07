package org.vaadin.viritin.testdomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Matti Tahvonen
 */
public class Service {

    static final Random r = new Random(0);

    static final List<Group> groups = Arrays.asList(new Group("Athletes"),
            new Group("Nerds"), new Group("Collegues"), new Group("Students"),
            new Group("Hunting club members"));
    
    
    public static Person getPerson() {
        Person p = new Person(123, "Matti", "Meikäläinen", 6);
        p.getGroups().add(groups.get(1));
        return p;
    }
    
    public static List<Group> getAvailableGroups() {
        return new ArrayList(groups);
    }

    public static List<Person> getListOfPersons(int total) {
        List<Person> l = new ArrayList<Person>(total);
        for (int i = 0; i < total; i++) {
            Person p = new Person();
            p.setId(i+1);
            p.setFirstName("First" + i);
            p.setLastName("Lastname" + i);
            p.setAge(r.nextInt(100));
            l.add(p);
        }
        return l;
    }

}
