package com.pocketflow.pocketflow_backend.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetResponse {

    private Long id;
    private Long categoryId;
    private String categoryName;
    private Integer month;
    private Integer year;
    private BigDecimal monthlyLimit;
    private BigDecimal actualSpent;
    private BigDecimal remainingBudget;
    private Double usagePercentage;
    private String status;
}
