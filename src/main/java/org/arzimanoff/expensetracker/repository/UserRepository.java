package org.arzimanoff.expensetracker.repository;

import org.arzimanoff.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}