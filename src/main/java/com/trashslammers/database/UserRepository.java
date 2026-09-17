package com.trashslammers.database;

import com.trashslammers.model.User;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);

    User save(User user);



}
