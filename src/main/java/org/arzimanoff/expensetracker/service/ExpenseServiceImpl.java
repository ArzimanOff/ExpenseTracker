package org.arzimanoff.expensetracker.service;

import jakarta.transaction.Transactional;
import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.repository.CategoryRepository;
import org.arzimanoff.expensetracker.repository.ExpenseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseServiceImpl.class);
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesForUser(User user) {
        return expenseRepository.findByUser(user, Sort.unsorted());
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteExpenseById(Long id, User user) {
        Optional<Expense> expenseOptional = expenseRepository.findByIdAndUser(id, user);
        if (expenseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Запись траты не найдена или у вас нет прав для её удаления.");
        }

        int deletedRows = expenseRepository.deleteByIdAndUser(id, user);
        if (deletedRows > 0) {
            return ResponseEntity.ok("Запись траты успешно удалена.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка удаления записи траты.");
        }
    }

    @Override
    public List<Expense> getExpensesByCategoryAndUser(Long categoryId, User user) {
        return expenseRepository.findByCategoryIdAndUser(categoryId, user, Sort.unsorted());
    }

    @Transactional
    @Override
    public ResponseEntity<Expense> updateExpense(
            Long id,
            Expense updatedExpense,
            User user
    ) {
        Optional<Expense> existingExpenseOptional = expenseRepository.findByIdAndUser(id, user);
        if (existingExpenseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Expense currentExpense = existingExpenseOptional.get();

        if (updatedExpense.getCategory() != null && updatedExpense.getCategory().getId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findByIdAndUser(updatedExpense.getCategory().getId(), user);
            if (categoryOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            currentExpense.setCategory(categoryOptional.get());
        }

        // Обновление полей
        currentExpense.setAmount(updatedExpense.getAmount());
        currentExpense.setDate(updatedExpense.getDate());
        currentExpense.setDescription(updatedExpense.getDescription());

        Expense savedExpense = expenseRepository.save(currentExpense);

        return ResponseEntity.ok(savedExpense);
    }

    @Override
    public List<Expense> getExpensesWithFilters(
            User user,
            Long categoryId,
            LocalDate startDate,
            LocalDate endDate,
            String sortDirection
    ) {
        Sort sort = Sort.unsorted();
        if ("asc".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Direction.ASC, "amount");
        } else if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(Sort.Direction.DESC, "amount");
        }

        if (endDate == null) {
            endDate = LocalDate.now();
        }
        if (categoryId != null && startDate != null) {
            return expenseRepository.findByUserAndCategoryIdAndDateBetween(user, categoryId, startDate, endDate, sort);
        } else if (categoryId != null) {
            return expenseRepository.findByUserAndCategoryId(user, categoryId, sort);
        } else if (startDate != null) {
            return expenseRepository.findByUserAndDateBetween(user, startDate, endDate, sort);
        } else {
            return expenseRepository.findByUser(user, sort);
        }
    }

    @Override
    public BigDecimal getTotalExpensesByDateRange(User user, LocalDate startDate, LocalDate endDate) {
        LocalDate effectiveStartDate = (startDate != null) ? startDate : LocalDate.of(1970, 1, 1);
        LocalDate effectiveEndDate = (endDate != null) ? endDate : LocalDate.now();
        logger.info("Calculating total expenses for user={} from {} to {}", user.getUsername(), effectiveStartDate, effectiveEndDate);
        List<Expense> expenses = expenseRepository.findByUserAndDateBetween(user, effectiveStartDate, effectiveEndDate, Sort.unsorted());
        BigDecimal total = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Total expenses: {}", total);
        return total;
    }

}
