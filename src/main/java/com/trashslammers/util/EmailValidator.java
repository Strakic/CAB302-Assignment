package com.trashslammers.util;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final Pattern email_pattern =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private EmailValidator(){
        // not instantiated
    }

    public static boolean isValid(String email){
        return email != null && email_pattern.matcher(email).matches();
    }

}
