package com.pocketflow.pocketflow_backend.service;

import com.pocketflow.pocketflow_backend.dto.BudgetRequest;
import com.pocketflow.pocketflow_backend.dto.BudgetResponse;
import com.pocketflow.pocketflow_backend.entity.Budget;
import com.pocketflow.pocketflow_backend.entity.Category;
import com.pocketflow.pocketflow_backend.entity.Transaction;
import com.pocketflow.pocketflow_backend.entity.TransactionType;
import com.pocketflow.pocketflow_backend.repository.BudgetRepository;
import com.pocketflow.pocketflow_backend.repository.CategoryRepository;
import com.pocketflow.pocketflow_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgetsByMonthAndYear(Integer month, Integer year){
        List<Budget> budgets = budgetRepository.findByMonthAndYear(month, year);
        return budgets.stream()
                .map(this::buildBudgetResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BudgetResponse setBudget(BudgetRequest request){
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category with ID " + request.getCategoryId() + " not found"));

        if(category.getType() != TransactionType.EXPENSE){
            throw new RuntimeException("Budget could only be implemented on expense category (EXPENSE)");
        }

        Budget budget = budgetRepository.findByCategoryIdAndMonthAndYear(request.getCategoryId(), request.getMonth(), request.getYear())
                .orElse(Budget.builder()
                        .category(category)
                        .month(request.getMonth())
                        .year(request.getYear())
                        .build());

        budget.setMonthlyLimit(request.getMonthlyLimit());
        Budget savedBudget = budgetRepository.save(budget);

        return buildBudgetResponse(savedBudget);
    }

    private BudgetResponse buildBudgetResponse(Budget budget){
        LocalDate startDate = LocalDate.of(budget.getYear(), budget.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        List<Transaction> monthlyTransactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);

        BigDecimal actualSpent = monthlyTransactions.stream()
                .filter(t -> t.getCategory().getId().equals(budget.getCategory().getId()))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal remainingBudget = budget.getMonthlyLimit().subtract(actualSpent);

        double usagePercentage = 0.0;
        if(budget.getMonthlyLimit().compareTo(BigDecimal.ZERO) > 0){
            usagePercentage = actualSpent.divide(budget.getMonthlyLimit(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        String status;
        if(actualSpent.compareTo(budget.getMonthlyLimit()) > 0) {
            status = "EXCEEDED";
        } else if(usagePercentage >= 80.0){
            status = "WARNING";
        } else {
            status = "SAFE";
        }

        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .month(budget.getMonth())
                .year(budget.getYear())
                .monthlyLimit(budget.getMonthlyLimit())
                .actualSpent(actualSpent)
                .remainingBudget(remainingBudget)
                .usagePercentage(usagePercentage)
                .status(status)
                .build();
    }
}
