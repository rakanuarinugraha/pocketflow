package com.pocketflow.pocketflow_backend.service;

import com.pocketflow.pocketflow_backend.dto.CategoryBreakdownResponse;
import com.pocketflow.pocketflow_backend.dto.FinancialSummaryResponse;
import com.pocketflow.pocketflow_backend.entity.Transaction;
import com.pocketflow.pocketflow_backend.entity.TransactionType;
import com.pocketflow.pocketflow_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public FinancialSummaryResponse getFinancialSummary(LocalDate startDate, LocalDate endDate){
        List<Transaction> transactions;
        if(startDate != null && endDate != null){
            transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        } else{
            transactions = transactionRepository.findAll();
        }

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getCategory().getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getCategory().getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        List<CategoryBreakdownResponse> breakdownList = calculateCategoryBreakdown(transactions, totalExpense);

        return FinancialSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .netBalance(netBalance)
                .totalTransactions(transactions.size())
                .categoryBreakdown(breakdownList)
                .build();
    }

    private List<CategoryBreakdownResponse> calculateCategoryBreakdown(List<Transaction> transactions, BigDecimal totalExpense) {
        List<CategoryBreakdownResponse> breakdownList = new ArrayList<>();

        Map<Long, List<Transaction>> expenseByCategory = transactions.stream()
                .filter(t -> t.getCategory().getType() == TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(t -> t.getCategory().getId()));

        for (Map.Entry<Long, List<Transaction>> entry : expenseByCategory.entrySet()){
            List<Transaction> categoryTxList = entry.getValue();
            String categoryName = categoryTxList.get(0).getCategory().getName();

            BigDecimal categoryTotal = categoryTxList.stream()
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            double percentage = 0.0;
            if (totalExpense.compareTo(BigDecimal.ZERO) > 0){
                percentage = categoryTotal.divide(totalExpense, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .doubleValue();
            }

            breakdownList.add(CategoryBreakdownResponse.builder()
                    .categoryId(entry.getKey())
                    .categoryName(categoryName)
                    .totalAmount(categoryTotal)
                    .percentage(percentage)
                    .build());
        }

        return breakdownList;
    }
}
