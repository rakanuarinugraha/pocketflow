package com.pocketflow.pocketflow_backend.controller;

import com.pocketflow.pocketflow_backend.dto.FinancialSummaryResponse;
import com.pocketflow.pocketflow_backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    public ResponseEntity<FinancialSummaryResponse> getFinancialSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        FinancialSummaryResponse summary = reportService.getFinancialSummary(startDate, endDate);
        return ResponseEntity.ok(summary);
    }
}
