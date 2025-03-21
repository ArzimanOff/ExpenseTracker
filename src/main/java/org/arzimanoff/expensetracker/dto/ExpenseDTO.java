package org.arzimanoff.expensetracker.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpenseDTO {
    private Long id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private CategoryDTO category;

    // Конструкторы, геттеры и сеттеры
    public ExpenseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public CategoryDTO getCategory() { return category; }
    public void setCategory(CategoryDTO category) { this.category = category; }
}
