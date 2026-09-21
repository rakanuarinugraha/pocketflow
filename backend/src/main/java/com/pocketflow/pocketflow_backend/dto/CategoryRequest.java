package com.pocketflow.pocketflow_backend.dto;

import com.pocketflow.pocketflow_backend.entity.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {

    @NotBlank(message = "Category name must be filled!")
    @Size(max = 100, message = "Category name max 100 character")
    private String name;

    @Size(max = 255, message = "Description max 255 character")
    private String description;

    @NotNull(message = "Category type must be filled (INCOME or EXPENSE)")
    private TransactionType type;
}
