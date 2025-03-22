package org.arzimanoff.expensetracker.repository;

import jakarta.transaction.Transactional;
import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndUser(Long id, User user);

    @Modifying
    @Query("DELETE FROM Expense e WHERE e.id = :id AND e.user = :user")
    int deleteByIdAndUser(@Param("id") Long id, @Param("user") User user);

    List<Expense> findByCategoryIdAndUser(Long categoryId, User user);

    //    @Query("SELECT e FROM Expense e WHERE e.user = :user " +
//           "AND ( :categoryId IS NULL OR e.category.id = :categoryId) " +
//           "AND ( :startDate IS NULL OR e.date >= CAST(:startDate AS date)) " +
//           "AND ( :endDate IS NULL OR e.date <= CAST(:endDate AS date))")
//    List<Expense> findByUserAndFilters(
//            @Param("user") User user,
//            @Param("categoryId") Long categoryId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );
    List<Expense> findByUserAndCategoryIdAndDateBetween(
            User user,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate);

    // Дополнительные методы для обработки null
    List<Expense> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Expense> findByUserAndCategoryId(User user, Long categoryId);

    List<Expense> findByUser(User user);
}