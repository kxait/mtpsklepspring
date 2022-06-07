package com.mytoporazka.lib.domain;

import com.mytoporazka.lib.interfaces.Serializable;

import java.util.List;

public class Product implements Serializable {
    public Product(String name, int productID, String description, double price, String manufacturer, boolean deleted) {
        this.name = name;
        this.productID = productID;
        this.description = description;
        this.price = price;
        this.manufacturer = manufacturer;
        this.deleted = deleted;
    }

    public String name;
    public int productID;
    public String description;
    public double price;
    public String manufacturer;
    public boolean deleted;

    @Override
    public List<String> serialize() {
        return List.of(name, ((Integer)productID).toString(), description, ((Double)price).toString(), manufacturer, ((Boolean)deleted).toString());
    }
}
