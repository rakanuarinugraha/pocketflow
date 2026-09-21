package com.pocketflow.pocketflow_backend.dto;

import com.pocketflow.pocketflow_backend.entity.Transaction;
import com.pocketflow.pocketflow_backend.entity.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private Long categoryId;
    private String categoryName;
    private TransactionType categoryType;
    private LocalDateTime createdAt;

    public static TransactionResponse fromEntity(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .categoryType(transaction.getCategory().getType())
                .createdAt(transaction.getCreatedAt())
                .build();

    }
}
