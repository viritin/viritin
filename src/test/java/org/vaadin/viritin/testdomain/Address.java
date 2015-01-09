package org.vaadin.viritin.testdomain;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Matti Tahvonen
 */
public class Address {
    
    public enum AddressType {
        Home, Work, Leisure, Other
    }

    private AddressType type = AddressType.Home;

    @NotNull
    private String street;

    @NotNull
    private String city;

    private Integer zipCode;

    public AddressType getType() {
        return type;
    }

    public void setType(AddressType type) {
        this.type = type;
    }

    /**
     * Get the value of street
     *
     * @return the value of street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Set the value of street
     *
     * @param street new value of street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Get the value of city
     *
     * @return the value of city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set the value of city
     *
     * @param city new value of city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get the value of zipCode
     *
     * @return the value of zipCode
     */
    public Integer getZipCode() {
        return zipCode;
    }

    /**
     * Set the value of zipCode
     *
     * @param zipCode new value of zipCode
     */
    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Address{" + "type=" + type + ", street=" + street + ", city=" + city + ", zipCode=" + zipCode + '}';
    }

    
    
}
