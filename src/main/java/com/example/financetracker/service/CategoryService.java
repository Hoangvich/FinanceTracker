package com.example.financetracker.service;

import com.example.financetracker.entity.Category;
import com.example.financetracker.exception.ResourceNotFoundException;
import com.example.financetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy category với id: " + id));
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category request) {
        Category existing = getCategoryById(id);
        existing.setName(request.getName());
        existing.setType(request.getType());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy category với id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}