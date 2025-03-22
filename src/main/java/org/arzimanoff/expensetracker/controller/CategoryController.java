package org.arzimanoff.expensetracker.controller;

import org.arzimanoff.expensetracker.dto.CategoryDTO;
import org.arzimanoff.expensetracker.dto.ExpenseDTO;
import org.arzimanoff.expensetracker.mapper.CategoryMapper;
import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
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
    public ResponseEntity<List<CategoryDTO>> getCategories(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<Category> categories = categoryService.getCategoriesForUser(user);
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(Authentication authentication, @PathVariable Long id) {
        User user = (User) authentication.getPrincipal();
        return categoryService.deleteCategoryById(id, user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody Category newCategory
    ){
        User user = (User) authentication.getPrincipal();
        ResponseEntity<Category> response = categoryService.updateCategory(id, newCategory, user);
        if (!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
        return ResponseEntity.ok(categoryMapper.toDTO(response.getBody()));
    }
}
