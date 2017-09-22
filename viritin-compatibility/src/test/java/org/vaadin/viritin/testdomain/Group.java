
package org.vaadin.viritin.testdomain;

/**
 *
 * @author Matti Tahvonen
 */
public class Group {
    
    static int counter = 0;
    
    private String name;
    private int iidee = counter++;

    public Group(String name) {
        this.name = name;
    }

    public Group() {
    }

    public int getIidee() {
        return iidee;
    }

    public void setIidee(int iidee) {
        this.iidee = iidee;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Group{" + "name=" + name + '}';
    }

}
