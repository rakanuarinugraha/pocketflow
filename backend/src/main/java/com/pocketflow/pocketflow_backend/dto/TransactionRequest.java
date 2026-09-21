package com.pocketflow.pocketflow_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    @NotNull(message = "Transaction amount must be filled")
    @Positive(message = "Transaction amount must be bigger than 0")
    private BigDecimal amount;

    @NotBlank(message = "Transaction description must be filled")
    private String description;

    private LocalDate transactionDate;

    @NotNull(message = "Category ID must be filled")
    private Long categoryId;
}
