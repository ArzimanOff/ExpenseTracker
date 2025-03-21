package org.arzimanoff.expensetracker.repository;

import jakarta.transaction.Transactional;
import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);

    Optional<Expense> findByIdAndUser(Long id, User user);

    @Modifying
    @Query("DELETE FROM Expense e WHERE e.id = :id AND e.user = :user")
    int deleteByIdAndUser(@Param("id") Long id, @Param("user") User user);

    List<Expense> findByCategoryIdAndUser(Long categoryId, User user);
}