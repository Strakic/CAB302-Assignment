package com.trashslammers.database;

import com.trashslammers.model.User;
import java.util.Optional;

/**
 * Stores and looks up user accounts; the login system now uses IUserDAO instead.
 */
public interface UserRepository {

    Optional<User> findByUsername(String username);

    User save(User user);



}
