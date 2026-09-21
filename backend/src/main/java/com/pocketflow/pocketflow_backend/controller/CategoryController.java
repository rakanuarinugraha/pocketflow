package com.pocketflow.pocketflow_backend.controller;

import com.pocketflow.pocketflow_backend.dto.CategoryRequest;
import com.pocketflow.pocketflow_backend.dto.CategoryResponse;
import com.pocketflow.pocketflow_backend.entity.TransactionType;
import com.pocketflow.pocketflow_backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@RequestParam(required = false)TransactionType type) {
        List<CategoryResponse> categories = categoryService.getAllCategories(type);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request){
        CategoryResponse createdCategory = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
