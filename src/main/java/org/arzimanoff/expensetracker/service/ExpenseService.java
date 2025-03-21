package org.arzimanoff.expensetracker.service;

import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense);
    List<Expense> getExpensesForUser(User user);
    ResponseEntity<String> deleteExpenseById(Long id, User user);
    List<Expense> getExpensesByCategoryAndUser(Long categoryId, User user);
}
