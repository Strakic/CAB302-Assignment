package com.trashslammers.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private static final int WORK_FACTOR = 12;

    private PasswordUtil() {
        // not instantiated
    }

    public static String hashPassword(String Password) {
        return BCrypt.hashpw(Password, BCrypt.gensalt(WORK_FACTOR));

    }

    public static boolean verifyPassword(String Password, String storedHash) {
        return BCrypt.checkpw(Password, storedHash);
    }



}
