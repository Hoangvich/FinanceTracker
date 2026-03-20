package com.example.financetracker.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;

    @NotBlank(message = "Description is required")
    private String description;

    private LocalDateTime transactionDate; // nếu null sẽ tự lấy thời gian hiện tại

    @NotBlank(message = "Category name is required")
    private String categoryName;

    @Pattern(regexp = "INCOME|EXPENSE", message = "Type must be INCOME or EXPENSE")
    private String categoryType;
}