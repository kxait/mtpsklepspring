package com.mytoporazka.lib.domain;

import com.mytoporazka.lib.interfaces.Serializable;

import java.time.Instant;
import java.util.List;

public class Order implements Serializable {
    public int userID;
    public int orderID;
    public Instant orderDate;
    public OrderStatus status;

    public Order(int userID, int orderID, Instant orderDate, OrderStatus status) {
        this.userID = userID;
        this.orderID = orderID;
        this.orderDate = orderDate;
        this.status = status;
    }

    @Override
    public List<String> serialize() {
        return List.of(((Integer)userID).toString(), ((Integer)orderID).toString(), orderDate.toString(), status.toString());
    }
}
