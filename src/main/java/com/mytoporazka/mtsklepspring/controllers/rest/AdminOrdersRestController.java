package com.mytoporazka.mtsklepspring.controllers.rest;

import com.mytoporazka.lib.domain.OrderStatus;
import com.mytoporazka.lib.domain.UserStatus;
import com.mytoporazka.lib.domain.UserType;
import com.mytoporazka.mtsklepspring.components.DatabaseRepository;
import com.mytoporazka.mtsklepspring.components.LoginSessionManager;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AdminOrdersRestController {
    private final DatabaseRepository repository;
    private final LoginSessionManager loginSession;

    public AdminOrdersRestController(DatabaseRepository repository, LoginSessionManager loginSession) {
        this.repository = repository;
        this.loginSession = loginSession;
    }

    @PostMapping("/api/admin/orders/{id}/setStatus")
    public void setUserState(@RequestHeader("id") String sessionId, @PathVariable("id") int orderId, @RequestBody OrderStatus orderStatus) {
        var thisUserId = getUserIdForSession(sessionId);
        if(thisUserId.isEmpty()) return;

        repository.order.modifyByPredicate(u -> u.orderID == orderId, u -> u.status = orderStatus);
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