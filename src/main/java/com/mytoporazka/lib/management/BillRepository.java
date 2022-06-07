package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.domain.Bill;

public class BillRepository extends AbstractDbRepository<Bill> {
    BillRepository() {
        super(Const.BILL_REPO_TAG);
    }

    @Override
    Bill cast(Object elem) {
        return (Bill) elem;
    }
}
