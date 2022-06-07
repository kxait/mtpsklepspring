package com.mytoporazka.lib.data;

import com.mytoporazka.lib.domain.*;
import com.mytoporazka.lib.management.ShopDbRepository;

import java.time.Instant;

public class MockShopDbFactory {
    public static ShopDbRepository getMock(String file) {
        var repo = new ShopDbRepository(file);

        repo.user.add(new User(0, "password", "a.b@c.com", UserStatus.ACTIVE,  UserType.CUSTOMER));
        repo.user.add(new User(0, "password", "b.c@d.com", UserStatus.ACTIVE,  UserType.ADMIN));
        repo.user.add(new User(1, "password", "c.d@e.com", UserStatus.NEW,  UserType.CUSTOMER));
        repo.user.add(new User(2, "password", "d.e@f.com", UserStatus.BLOCKED,  UserType.CUSTOMER));

        repo.product.add(new Product("Dres MTP", 0, "welurowy :D", 499.99, "MTP", false));
        repo.product.add(new Product("Czapka MTP", 1, "bawelniana :D", 119.99, "MTP", false));
        repo.product.add(new Product("Odzywka bialkowa", 2, "sojowa :D", 120.0, "SFD", false));
        repo.product.add(new Product("Naklejki", 3, "kolorowe :D", 10.0, "MTP", false));

        repo.order.add(new Order(0, 0, Instant.now(), OrderStatus.NEW));
        repo.order.add(new Order(0, 1, Instant.now(), OrderStatus.PROCESSING));
        repo.order.add(new Order(0, 2, Instant.now(), OrderStatus.SHIPPING));
        repo.order.add(new Order(0, 3, Instant.now(), OrderStatus.DELIVERED));
        repo.order.add(new Order(0, 4, Instant.now(), OrderStatus.CANCELLED));

        repo.bill.add(new Bill(0, Instant.now(), Instant.MAX, 0.0));
        repo.bill.add(new Bill(1, Instant.now(), Instant.MAX, 0.0));
        repo.bill.add(new Bill(2, Instant.now(), Instant.MAX, 0.0));
        repo.bill.add(new Bill(3, Instant.now(), Instant.MAX, 0.0));
        repo.bill.add(new Bill(4, Instant.now(), Instant.MAX, 0.0));

        repo.address.add(new Address(0, "Krakowskie Przedmiescie", "48/50", "00-071", "Warszawa", "Polska", "123456789"));
        repo.address.add(new Address(1, "Krakowskie Przedmiescie", "48/50", "00-071", "Warszawa", "Polska", "123456789"));
        repo.address.add(new Address(2, "Krakowskie Przedmiescie", "48/50", "00-071", "Warszawa", "Polska", "123456789"));
        repo.address.add(new Address(3, "Krakowskie Przedmiescie", "48/50", "00-071", "Warszawa", "Polska", "123456789"));
        repo.address.add(new Address(4, "Krakowskie Przedmiescie", "48/50", "00-071", "Warszawa", "Polska", "123456789"));

        repo.orderEntry.add(new OrderEntry(10, 0, 0));
        repo.orderEntry.add(new OrderEntry(20, 1, 0));
        repo.orderEntry.add(new OrderEntry(30, 2, 0));
        repo.orderEntry.add(new OrderEntry(40, 3, 0));
        repo.orderEntry.add(new OrderEntry(50, 4, 0));

        return repo;
    }
}
