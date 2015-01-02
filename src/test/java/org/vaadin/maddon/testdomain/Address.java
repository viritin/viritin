package org.vaadin.maddon.testdomain;

import javax.validation.constraints.NotNull;

/**
 *
 * @author Matti Tahvonen
 */
public class Address {

    private String type = "home";

    @NotNull
    private String street;

    @NotNull
    private String city;

    private Integer zipCode;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(String type) {
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

}
