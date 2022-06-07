package com.mytoporazka.lib.domain;

import com.mytoporazka.lib.interfaces.Serializable;

import java.util.List;

public class OrderEntry implements Serializable {
    public int quantity;
    public int orderID;
    public int productID;

    public OrderEntry(int quantity, int orderID, int productID) {
        this.quantity = quantity;
        this.orderID = orderID;
        this.productID = productID;
    }

    private static String itos(int a) { return ((Integer)a).toString(); }

    @Override
    public List<String> serialize() {
        return List.of(itos(quantity), itos(orderID), itos(productID));
    }
}
