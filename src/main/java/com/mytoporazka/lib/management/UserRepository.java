package com.mytoporazka.lib.management;

import com.mytoporazka.lib.Const;
import com.mytoporazka.lib.domain.User;

public class UserRepository extends AbstractDbRepository<User>{
    UserRepository() {
        super(Const.USER_REPO_TAG);
    }

    @Override
    User cast(Object elem) {
        return (User)elem;
    }
}