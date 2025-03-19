package org.arzimanoff.expensetracker.service;

import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;

import java.util.List;

public interface ExpenseService {
    Expense addExpense(Expense expense);
    List<Expense> getExpensesForUser(User user);
}
