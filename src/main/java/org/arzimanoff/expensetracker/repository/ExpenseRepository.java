package org.arzimanoff.expensetracker.repository;

import org.arzimanoff.expensetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}