package org.arzimanoff.expensetracker.repository;


import jakarta.transaction.Transactional;
import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Transactional
    List<Category> findByUser(User user);

    @Transactional
    Optional<Category> findByIdAndUser(Long id, User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM Category c WHERE c.id = :id AND c.user = :user")
    void deleteByIdAndUser(
            @Param("id") Long id,
            @Param("user") User user
    );



}