package com.trashslammers.model.usertype;

import com.trashslammers.model.User;

/**
 * A premium user, who earns double points for sorting trash.
 */
public class UserPremium extends User {
    /**
     * Constructs a new UserPremium with the specified username and password hash.
     * @param username The username of the user
     * @param passwordHash The hashed password of the user
     */

    public UserPremium(String username, String passwordHash) {
        super(username, passwordHash);
    }

    @Override
    public Role getRole() {
        return Role.PREMIUM;
    }

    @Override
    public int getPointMultiplyer() {
        return 2;
    }

}
