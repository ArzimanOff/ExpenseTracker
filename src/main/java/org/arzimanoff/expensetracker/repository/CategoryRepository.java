package org.arzimanoff.expensetracker.repository;


import org.arzimanoff.expensetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}