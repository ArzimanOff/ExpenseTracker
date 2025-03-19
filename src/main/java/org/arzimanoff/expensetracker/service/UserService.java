package org.arzimanoff.expensetracker.service;

import org.arzimanoff.expensetracker.model.User;

public interface UserService {
    User registerUser(User user);
    User findByUsername(String username);
}
