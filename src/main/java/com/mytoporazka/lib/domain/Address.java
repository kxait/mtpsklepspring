package com.mytoporazka.lib.domain;

import com.mytoporazka.lib.interfaces.Serializable;

import java.util.List;

public class Address implements Serializable {
    public int orderID;
    public String street;
    public String streetNumber;
    public String postalCode;
    public String city;
    public String country;
    public String phone;

    public Address(int orderID, String street, String streetNumber, String postalCode, String city, String country, String phone) {
        this.orderID = orderID;
        this.street = street;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.phone = phone;
    }

    @Override
    public List<String> serialize() {
        return List.of(((Integer)orderID).toString(), street, streetNumber, postalCode, city, country, phone);
    }
}
