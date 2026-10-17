package com.trashslammers.util;

import java.util.regex.Pattern;

/**
 * Checks whether a username is a valid email address.
 */
public class EmailValidator {

    private static final Pattern email_pattern =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private EmailValidator(){
        // not instantiated
    }

    /**
     * Checks whether text is a valid email address.
     * @param email The text to check
     * @return true if the text is a valid email address, otherwise false
     */
    public static boolean isValid(String email){
        return email != null && email_pattern.matcher(email).matches();
    }

}
