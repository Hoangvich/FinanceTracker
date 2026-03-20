package com.example.financetracker.controller;

import com.example.financetracker.dto.request.CategoryRequest;
import com.example.financetracker.dto.response.ApiResponse;
import com.example.financetracker.entity.Category;
import com.example.financetracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<Category>> getAll() {
        return ApiResponse.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> getById(@PathVariable Long id) {
        return ApiResponse.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ApiResponse<Category> create(@Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        return ApiResponse.ok(categoryService.createCategory(category));
    }

    @PutMapping("/{id}")
    public ApiResponse<Category> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        return ApiResponse.ok(categoryService.updateCategory(id, category));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.ok(null);
    }
}