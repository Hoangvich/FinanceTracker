package com.example.financetracker.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT)  // luôn hiển thị dạng 15000000.0
    private Double amount;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // format ngày giờ cho dễ đọc
    private LocalDateTime transactionDate;

    private String categoryName;
    private String categoryType;
}