package com.trashslammers.model.usertype;

import com.trashslammers.model.User;

public class UserPremium extends User {
    /**
     * Creates a brand-new user that has not been persisted yet.
     * The id is assigned later by whatever saves it (see UserRepository).
     *
     * @param username
     * @param passwordHash
     */
    public UserPremium(String username, String passwordHash) {
        super(username, passwordHash);
    }
}
