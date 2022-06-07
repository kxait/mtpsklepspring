package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.domain.OrderEntry;

public class OrderEntryRepository extends AbstractDbRepository<OrderEntry> {
    OrderEntryRepository() {
        super(Const.ORDER_ENTRY_REPO_TAG);
    }

    @Override
    OrderEntry cast(Object elem) {
        return (OrderEntry) elem;
    }
}
