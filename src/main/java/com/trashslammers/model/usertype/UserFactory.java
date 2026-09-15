package com.trashslammers.model.usertype;

import com.trashslammers.model.User;

public class UserFactory {

    private UserFactory() {

    }

    public static User create(Role role, String username, String passwordHash) {
        switch (role) {
            case ADMIN:
                return new UserAdmin(username, passwordHash);

            case PREMIUM:
                return new UserPremium(username, passwordHash);

            case STANDARD:
                return new User(username, passwordHash);

            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }

    public static User create(Role role, int id, String username, String passwordHash) {
        User user = create(role, username, passwordHash);
        user.setId(id);
        return user;
    }




}
