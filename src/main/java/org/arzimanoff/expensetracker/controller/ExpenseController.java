package org.arzimanoff.expensetracker.controller;

import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.service.CategoryService;
import org.arzimanoff.expensetracker.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public ExpenseController(ExpenseService expenseService, CategoryService categoryService) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(
            @RequestBody Expense expense,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        expense.setUser(user);

        // Загружаем полный объект Category по id
        Category category = categoryService.getCategoryById(expense.getCategory().getId());
        if (category == null) {
            return ResponseEntity.badRequest().body(null); // Или выбросить исключение
        }
        expense.setCategory(category);

        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Expense> expenses = expenseService.getExpensesForUser(user);
        return ResponseEntity.ok(expenses);
    }
}
