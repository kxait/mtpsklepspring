package com.mytoporazka.mtsklepspring.components;

import com.mytoporazka.lib.domain.UserStatus;
import com.mytoporazka.lib.domain.UserType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LoginSessionManager {
    private final DatabaseRepository repository;

    private final Map<String, Integer> userIdsBySessionId;

    private int nextId = 0;

    private static final String ENCODE_KEY = "Digital signatures enhance the ability of contracting parties\n" +
            "to authenticate electronic communication.";

    public LoginSessionManager(DatabaseRepository repository) {
        this.repository = repository;

        this.userIdsBySessionId = new HashMap<>();
    }

    public Optional<String> getNewSession(String email, String password) {
        var user = repository.user.getFirstByPredicate(c ->
                c.email.equals(email)
                && c.password.equals(password)
                && c.status == UserStatus.ACTIVE);

        if(user == null) {
            return Optional.empty();
        }

        var sessionId = encodeSessionId(nextId++);
        userIdsBySessionId.put(sessionId, user.userId);

        return Optional.of(sessionId);
    }

    public boolean isAdmin(String sessionId) {
        var exists = userIdsBySessionId.containsKey(sessionId);
        if(!exists) return false;

        var userId = userIdsBySessionId.get(sessionId);
        var user = repository.user.getFirstByPredicate(c -> c.userId == userId);
        return user.userType == UserType.ADMIN;
    }

    public Optional<Integer> getUserIdBySessionId(String sessionId) {
        if(!userIdsBySessionId.containsKey(sessionId))
            return Optional.empty();


        var userId = userIdsBySessionId.get(sessionId);
        var user = repository.user.getFirstByPredicate(u -> u.userId == userId);

        if(user.status != UserStatus.ACTIVE)
            return Optional.empty();

        return Optional.of(userId);
    }

    public boolean existsSession(String sessionId) {
        if(!userIdsBySessionId.containsKey(sessionId)) return false;
        var userId = getUserIdBySessionId(sessionId);
        if(userId.isEmpty()) return false;
        var user = repository.user.getFirstByPredicate(u -> u.userId == userId.get());
        return user.status == UserStatus.ACTIVE;
    }

    private String encodeSessionId(int newId) {
        var referenceBytes= ENCODE_KEY.getBytes();
        long result = 0;

        for (byte referenceByte : referenceBytes) {
            result += referenceByte ^ newId;
        }

        result += System.currentTimeMillis();

        return ((Long)result).toString();
    }
}
