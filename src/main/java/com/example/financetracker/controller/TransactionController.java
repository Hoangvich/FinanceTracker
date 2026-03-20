package com.example.financetracker.controller;

import com.example.financetracker.dto.request.TransactionRequest;
import com.example.financetracker.dto.response.ApiResponse;
import com.example.financetracker.dto.response.TransactionResponse;
import com.example.financetracker.entity.User;
import com.example.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ApiResponse<List<TransactionResponse>> getAll(
            @AuthenticationPrincipal User user) {
        return ApiResponse.ok(transactionService.getAllTransactions(user));
    }

    @PostMapping
    public ApiResponse<TransactionResponse> create(
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal User user) {
        return ApiResponse.ok(transactionService.createTransaction(request, user));
    }

    @PutMapping("/{id}")
    public ApiResponse<TransactionResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request,
            @AuthenticationPrincipal User user) {
        return ApiResponse.ok(transactionService.updateTransaction(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        transactionService.deleteTransaction(id, user);
        return ApiResponse.ok(null);
    }

    @GetMapping("/summary")
    public ApiResponse<Map<String, Double>> getSummary(
            @AuthenticationPrincipal User user) {
        double income = transactionService.getTotalByType("INCOME", user);
        double expense = transactionService.getTotalByType("EXPENSE", user);
        return ApiResponse.ok(Map.of(
                "income", income,
                "expense", expense,
                "balance", income - expense
        ));
    }

    @GetMapping("/filter")
    public ApiResponse<List<TransactionResponse>> getWithFilters(
            @RequestParam(required = false) String type,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @AuthenticationPrincipal User user) {
        return ApiResponse.ok(
                transactionService.getTransactionsWithFilters(type, from, to, user)
        );
    }
}
