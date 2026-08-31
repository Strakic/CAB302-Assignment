package com.trashslammers.database;

import com.trashslammers.model.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    @Override
    public User save(User user) {
        if (user.getId() == -1) {
            user.setId(nextId.getAndIncrement());
        }
        usersByUsername.put(user.getUsername(), user);
        return user;
    }
}