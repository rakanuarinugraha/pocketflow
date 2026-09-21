package com.pocketflow.pocketflow_backend.dto;

import com.pocketflow.pocketflow_backend.entity.Category;
import com.pocketflow.pocketflow_backend.entity.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private TransactionType type;
    private LocalDateTime createdAt;

    public static CategoryResponse fromEntity(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .type(category.getType())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
