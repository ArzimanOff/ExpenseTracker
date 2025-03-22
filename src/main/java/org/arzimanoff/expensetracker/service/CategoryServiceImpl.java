package org.arzimanoff.expensetracker.service;

import jakarta.transaction.Transactional;
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
public class CategoryServiceImpl implements CategoryService {

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

    @Transactional
    @Override
    public ResponseEntity<String> deleteCategoryById(Long id, User user) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndUser(id, user);
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Категория трат не найдена или у вас нет прав для её удаления.");
        }

        Category category = categoryOptional.get();
        if (!category.getExpenses().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Нельзя удалить категорию, если есть траты, относящиеся к этой категории");
        }

        int deletedRows = categoryRepository.deleteByIdAndUser(id, user);
        if (deletedRows > 0) {
            return ResponseEntity.ok("Категория трат успешно удалена");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка удаления категории.");
        }
    }

    @Override
    public Optional<Category> findCategoryByIdAndUser(Long id, User user) {
        return categoryRepository.findByIdAndUser(id, user);
    }

    @Override
    public ResponseEntity<Category> updateCategory(
            Long id,
            Category updatedCategory,
            User user
    ) {
        Optional<Category> optionalCategory = categoryRepository.findByIdAndUser(id, user);
        if (optionalCategory.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Category thisCategory = optionalCategory.get();
        thisCategory.setName(updatedCategory.getName());

        Category savedCategory = categoryRepository.save(thisCategory);

        return ResponseEntity.ok(savedCategory);
    }
}
