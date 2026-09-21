package com.pocketflow.pocketflow_backend.controller;

import com.pocketflow.pocketflow_backend.dto.BudgetRequest;
import com.pocketflow.pocketflow_backend.dto.BudgetResponse;
import com.pocketflow.pocketflow_backend.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(@RequestParam Integer month, @RequestParam Integer year){
        List<BudgetResponse> budgets = budgetService.getBudgetsByMonthAndYear(month, year);
        return ResponseEntity.ok(budgets);
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> setBudget(@Valid @RequestBody BudgetRequest request){
        BudgetResponse response = budgetService.setBudget(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
