package com.trashslammers.model.usertype;

import com.trashslammers.model.User;
/**
 * An admin user, who can also add new animals to the shop.
 */
public class UserAdmin extends User {
    /**
     * Constructs a new UserAdmin with the specified username and password hash.
     * @param username The username of the admin
     * @param passwordHash The hashed password of the admin
     */

    public UserAdmin(String username, String passwordHash) {
        super(username, passwordHash);
    }

    @Override
    public Role getRole(){
        return Role.ADMIN;
    }

    @Override
    public boolean canManageAnimals() {
        return true;
    }



}
