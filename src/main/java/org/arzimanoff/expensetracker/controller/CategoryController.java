package org.arzimanoff.expensetracker.controller;

import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(
            @RequestBody Category category,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        category.setUser(user);
        Category savedCategory = categoryService.addCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Category> categories = categoryService.getCategoriesForUser(user);
        return ResponseEntity.ok(categories);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return categoryService.deleteCategoryById(id, user);
    }
}
