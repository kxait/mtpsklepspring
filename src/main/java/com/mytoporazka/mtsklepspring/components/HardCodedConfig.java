package com.mytoporazka.mtsklepspring.components;

import com.mytoporazka.mtsklepspring.components.interfaces.Config;

public class HardCodedConfig implements Config {
    @Override
    public String getDbFileName() {
        return "db.lol";
    }

    @Override
    public double getPostagePrice() { return 10.99; }
}
