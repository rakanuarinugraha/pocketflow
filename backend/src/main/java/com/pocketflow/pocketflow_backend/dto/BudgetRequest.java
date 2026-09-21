package com.pocketflow.pocketflow_backend.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequest {

    @NotNull(message = "Category ID must be filled")
    private Long categoryId;

    @NotNull(message = "Monthly budget limit must be filled")
    @Positive(message = "Monthly budget limit must be above 0")
    private BigDecimal monthlyLimit;

    @NotNull(message = "Month must be filled (1-12)")
    @Min(value = 1, message = "Min 1")
    @Max(value = 12, message = "Max 12")
    private Integer month;

    @NotNull(message = "Year must be filled")
    @Min(value = 2000, message = "Min year: 2000")
    private Integer year;
}
