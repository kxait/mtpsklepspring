package com.mytoporazka.mtsklepspring.components.interfaces;

import org.springframework.stereotype.Component;

public interface Config {
    String getDbFileName();

    double getPostagePrice();
}
