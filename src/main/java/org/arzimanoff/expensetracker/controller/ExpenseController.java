package org.arzimanoff.expensetracker.controller;

import org.arzimanoff.expensetracker.dto.ExpenseDTO;
import org.arzimanoff.expensetracker.mapper.ExpenseMapper;
import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.service.CategoryService;
import org.arzimanoff.expensetracker.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<?> addExpense(
            @RequestBody Expense expense,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        expense.setUser(user);

        if (expense.getCategory() == null || expense.getCategory().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<Category> categoryOptional = categoryService.findCategoryByIdAndUser(expense.getCategory().getId(), user);
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Такой категории у вас нет!");
        }

        expense.setCategory(categoryOptional.get());

        Expense savedExpense = expenseService.addExpense(expense);
        return ResponseEntity.ok(
                expenseMapper.toDTO(savedExpense)
        );
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpenses(
            Authentication authentication,
            @RequestParam(value = "categoryId", required = false) Long categoryId
    ) {
        User user = (User) authentication.getPrincipal();
        List<Expense> expenses;
        if (categoryId != null) {
            if (categoryService.getCategoryById(categoryId) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.emptyList());
            }
            expenses = expenseService.getExpensesByCategoryAndUser(categoryId, user);
        } else {
            expenses = expenseService.getExpensesForUser(user);
        }

        List<ExpenseDTO> expenseDTOs = expenses.stream()
                .map(expenseMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(expenseDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(
            Authentication authentication,
            @PathVariable Long id
    ) {
        User user = (User) authentication.getPrincipal();
        return expenseService.deleteExpenseById(id, user);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> updateExpense(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Expense newExpense
    ) {
        User user = (User) authentication.getPrincipal();
        ResponseEntity<Expense> response = expenseService.updateExpense(id, newExpense, user);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
        return ResponseEntity.ok(expenseMapper.toDTO(response.getBody()));
    }

}
