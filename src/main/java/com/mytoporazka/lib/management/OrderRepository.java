package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.domain.Order;

public class OrderRepository extends AbstractDbRepository<Order> {
    OrderRepository() {
        super(Const.ORDER_REPO_TAG);
    }

    @Override
    Order cast(Object elem) {
        return (Order)elem;
    }
}
