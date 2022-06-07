package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.domain.Product;

public class ProductRepository extends AbstractDbRepository<Product> {
    ProductRepository() {
        super(Const.PRODUCT_REPO_TAG);
    }

    @Override
    Product cast(Object elem) {
        return (Product)elem;
    }
}
