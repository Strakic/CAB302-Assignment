package com.trashslammers.service;

import com.trashslammers.model.User;

public interface IAuthenticationService {
    User signUp(String username, String password);
    User logIn(String username, String password);

    User upgradeToPremium(User user);
}
// USER BRYN
// USER ARRAN
// USER JOHN
// USER ALESSIA