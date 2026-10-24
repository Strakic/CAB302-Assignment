package com.trashslammers.model.usertype;

import com.trashslammers.model.User;
/**
 * Creates the right type of user for a role.
 */
public class UserFactory {

    /**
     * Private constructor so the class can't be created, since every method is static.
     */
    private UserFactory() {

    }
    /**
     * Creates a new user of the type that matches the role.
     * @param role The role of the user
     * @param username The username of the user
     * @param passwordHash The hashed password of the user
     * @return A UserAdmin, UserPremium or standard User
     */
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

    /**
     * Creates a user of the type that matches the role, with an existing id.
     * @param role The role of the user
     * @param id The id of the user
     * @param username The username of the user
     * @param passwordHash The hashed password of the user
     * @return A UserAdmin, UserPremium or standard User with its id set
     */
    public static User create(Role role, int id, String username, String passwordHash) {
        User user = create(role, username, passwordHash);
        user.setId(id);
        return user;
    }




}
