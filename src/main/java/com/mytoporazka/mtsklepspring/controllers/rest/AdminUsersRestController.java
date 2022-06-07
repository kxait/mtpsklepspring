package com.mytoporazka.mtsklepspring.controllers.rest;

import com.mytoporazka.lib.domain.UserStatus;
import com.mytoporazka.lib.domain.UserType;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AdminUsersRestController {
    private final DatabaseRepository repository;
    private final LoginSessionManager loginSession;

    public AdminUsersRestController(DatabaseRepository repository, LoginSessionManager loginSession) {
        this.repository = repository;
        this.loginSession = loginSession;
    }

    @PostMapping("/api/admin/users/{id}/setStatus")
    public void setUserState(@RequestHeader("id") String sessionId, @PathVariable("id") int userId, @RequestBody UserStatus userStatus) {
        var thisUserId = getUserIdForSession(sessionId);
        if(thisUserId.isEmpty()) return;
        if(thisUserId.get() == userId) return;

        repository.user.modifyByPredicate(u -> u.userId == userId, u -> u.status = userStatus);
        repository.saveToDb();
    }

    @PostMapping("/api/admin/users/{id}/setType")
    public void setUserType(@RequestHeader("id") String sessionId, @PathVariable("id") int userId, @RequestBody UserType userType) {
        var thisUserId = getUserIdForSession(sessionId);
        if(thisUserId.isEmpty()) return;
        if(thisUserId.get() == userId) return;

        repository.user.modifyByPredicate(u -> u.userId == userId, u -> u.userType = userType);
        repository.saveToDb();
    }

    private Optional<Integer> getUserIdForSession(String session) {
        if(!loginSession.existsSession(session)) return Optional.empty();
        var userId = loginSession.getUserIdBySessionId(session);

        // wtf??
        if(userId.isEmpty()) return Optional.empty();

        var user = repository.user.getFirstByPredicate(u -> u.userId == userId.get());
        if(user.userType != UserType.ADMIN) return Optional.empty();

        return userId;
    }
}
