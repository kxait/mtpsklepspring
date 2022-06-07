package com.mytoporazka.lib.domain;

import com.mytoporazka.lib.interfaces.Serializable;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public class Bill implements Serializable {
    public int orderID;
    public Instant issueDate;
    public Instant dueDate;
    public double sum;

    public Bill(int orderID, Instant issueDate, Instant dueDate, double sum) {
        this.orderID = orderID;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.sum = sum;
    }

    @Override
    public List<String> serialize() {
        return List.of(((Integer)orderID).toString(), issueDate.toString(), dueDate.toString(), ((Double)sum).toString());
    }
}
