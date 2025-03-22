package org.arzimanoff.expensetracker.mapper;

import org.arzimanoff.expensetracker.dto.CategoryDTO;
import org.arzimanoff.expensetracker.dto.ExpenseDTO;
import org.arzimanoff.expensetracker.model.Category;
import org.arzimanoff.expensetracker.model.Expense;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryDTO toDTO(Category category){
        if (category == null){
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }
}
