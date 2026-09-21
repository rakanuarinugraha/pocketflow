package com.pocketflow.pocketflow_backend.service;

import com.pocketflow.pocketflow_backend.dto.CategoryRequest;
import com.pocketflow.pocketflow_backend.dto.CategoryResponse;
import com.pocketflow.pocketflow_backend.entity.Category;
import com.pocketflow.pocketflow_backend.entity.TransactionType;
import com.pocketflow.pocketflow_backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(TransactionType type){
        List<Category> categories;
        if(type != null){
            categories = categoryRepository.findByType(type);
        } else{
            categories = categoryRepository.findAll();
        }

        return categories.stream()
                .map(CategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse getCategoryById(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category with ID " + id + " not found"));
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public CategoryResponse createCategory(CategoryRequest request){
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long id){
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category with ID " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }
}
