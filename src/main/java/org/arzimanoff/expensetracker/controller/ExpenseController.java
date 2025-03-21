package org.arzimanoff.expensetracker.controller;

import org.arzimanoff.expensetracker.dto.ExpenseDTO;
import org.arzimanoff.expensetracker.mapper.ExpenseMapper;
import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.service.CategoryService;
import org.arzimanoff.expensetracker.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService expenseService;
    private final CategoryService categoryService;
    private final ExpenseMapper expenseMapper;

    public ExpenseController(ExpenseService expenseService, CategoryService categoryService, ExpenseMapper expenseMapper) {
        this.expenseService = expenseService;
        this.categoryService = categoryService;
        this.expenseMapper = expenseMapper;
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(
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
        return ResponseEntity.ok(
                expenseMapper.toDTO(savedExpense)
        );
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpenses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Expense> expenses = expenseService.getExpensesForUser(user);
        List<ExpenseDTO> expenseDTOs = expenses.stream()
                .map(expenseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(expenseDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(
            Authentication authentication,
            @PathVariable Long id
            ){
        User user = (User) authentication.getPrincipal();
        return expenseService.deleteExpenseById(id, user);
    }
}
