package com.mytoporazka.mtsklepspring.components;

import com.mytoporazka.lib.management.ShopDbRepository;
import com.mytoporazka.mtsklepspring.components.interfaces.Config;
import org.springframework.stereotype.Component;

@Component
public class DatabaseRepository extends ShopDbRepository {

    public DatabaseRepository(Config config) {
        super(config.getDbFileName());
        loadFromDb();
    }
}
