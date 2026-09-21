package com.pocketflow.pocketflow_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBreakdownResponse {
    private Long categoryId;
    private String categoryName;
    private BigDecimal totalAmount;
    private Double percentage;
}
