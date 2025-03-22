package org.arzimanoff.expensetracker.service;

import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category addCategory(Category category);
    List<Category> getCategoriesForUser(User user);
    Category getCategoryById(Long id);
    ResponseEntity<String> deleteCategoryById(Long id, User user);
    Optional<Category> findCategoryByIdAndUser(Long id, User user);
    ResponseEntity<Category> updateCategory(Long id, Category updatedCategory, User user);
}
