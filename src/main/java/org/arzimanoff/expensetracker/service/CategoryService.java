package org.arzimanoff.expensetracker.service;

import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getCategoriesForUser(User user);
    Category getCategoryById(Long id);
    ResponseEntity<String> deleteCategoryById(Long id, User user);
}
