package com.mytoporazka.lib.domain;

import com.mytoporazka.lib.interfaces.Serializable;

import java.util.List;

public class User implements Serializable {
    public int userId;
    public String password;
    public String email;
    public UserStatus status;
    public UserType userType;

    public User(int userId, String password, String email, UserStatus status, UserType userType) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.status = status;
        this.userType = userType;
    }

    @Override
    public List<String> serialize() {
        return List.of(
                ((Integer)userId).toString(),
                password,
                email,
                status.toString(),
                userType.toString()
        );
    }
}
