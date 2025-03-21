package org.arzimanoff.expensetracker.service;

import jakarta.transaction.Transactional;
import org.arzimanoff.expensetracker.model.Expense;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public List<Expense> getExpensesForUser(User user) {
        return expenseRepository.findByUser(user);
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

}
