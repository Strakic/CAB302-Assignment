package com.trashslammers.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Hashes and checks passwords with BCrypt.
 */
public final class PasswordUtil {

    private static final int WORK_FACTOR = 12;

    private PasswordUtil() {
        // not instantiated
    }

    /**
     * Hashes a password with BCrypt.
     * @param Password The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String Password) {
        return BCrypt.hashpw(Password, BCrypt.gensalt(WORK_FACTOR));

    }

    /**
     * Checks whether a password matches a stored hash.
     * @param Password The plain text password
     * @param storedHash The hashed password from the database
     * @return true if the password matches, otherwise false
     */
    public static boolean verifyPassword(String Password, String storedHash) {
        return BCrypt.checkpw(Password, storedHash);
    }



}
