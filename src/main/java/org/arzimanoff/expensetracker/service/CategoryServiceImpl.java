package org.arzimanoff.expensetracker.service;

import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.User;
import org.arzimanoff.expensetracker.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategoriesForUser(User user) {
        return categoryRepository.findByUser(user);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseEntity<?> deleteCategoryById(Long id, User user) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndUser(id, user);
        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            categoryRepository.deleteByIdAndUser(id, user);
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body("Такой категории не существует или у вас нет прав для её удаления!");
        }
    }
}
