package com.trashslammers.service;

import com.trashslammers.model.User;

public interface IAuthenticationService {
    User signUp(String username, String password);
    void logIn(String username, String password);
}
// USER BRYN
// USER ARRAN
// USER JOHN
// USER ALESSIA