package com.epam.rd.autocode.assessment.appliances.service;

import com.epam.rd.autocode.assessment.appliances.dto.CategoryDTO;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    Page<CategoryDTO> getAllCategories(Pageable pageable);
    Category createCategory(Category category);
    void deleteCategory(Long id);
    Optional<Category> getById(Long id);
}
