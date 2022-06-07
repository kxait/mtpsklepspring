package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.domain.Address;

public class AddressRepository extends AbstractDbRepository<Address> {
    AddressRepository() {
        super(Const.ADDRESS_REPO_TAG);
    }

    @Override
    Address cast(Object elem) {
        return (Address)elem;
    }
}
