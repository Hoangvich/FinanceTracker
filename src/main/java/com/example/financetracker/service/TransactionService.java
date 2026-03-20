package com.example.financetracker.service;

import com.example.financetracker.dto.request.TransactionRequest;
import com.example.financetracker.dto.response.TransactionResponse;
import com.example.financetracker.entity.Category;
import com.example.financetracker.entity.Transaction;
import com.example.financetracker.entity.User;
import com.example.financetracker.exception.ResourceNotFoundException;
import com.example.financetracker.repository.CategoryRepository;
import com.example.financetracker.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public List<TransactionResponse> getAllTransactions(User user) {
        return transactionRepository.findByUser(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse createTransaction(TransactionRequest request, User user) {
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(
                request.getTransactionDate() != null
                        ? request.getTransactionDate()
                        : LocalDateTime.now()
        );
        transaction.setCategory(
                findOrCreateCategory(request.getCategoryName(), request.getCategoryType())
        );
        transaction.setUser(user);
        return toResponse(transactionRepository.save(transaction));
    }

    public TransactionResponse updateTransaction(Long id, TransactionRequest request, User user) {
        Transaction existing = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giao dịch với id: " + id));

        existing.setAmount(request.getAmount());
        existing.setDescription(request.getDescription());
        existing.setTransactionDate(
                request.getTransactionDate() != null
                        ? request.getTransactionDate()
                        : existing.getTransactionDate()
        );
        existing.setCategory(
                findOrCreateCategory(request.getCategoryName(), request.getCategoryType())
        );
        return toResponse(transactionRepository.save(existing));
    }

    public void deleteTransaction(Long id, User user) {
        Transaction transaction = transactionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giao dịch với id: " + id));
        transactionRepository.delete(transaction);
    }

    public Double getTotalByType(String type, User user) {
        Double total = transactionRepository.sumTotalByTypeAndUser(type.toUpperCase(), user);
        return total != null ? total : 0.0;
    }

    public List<TransactionResponse> getTransactionsWithFilters(
            String type, LocalDateTime from, LocalDateTime to, User user) {
        return transactionRepository.findWithFilters(user, type, from, to)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Category findOrCreateCategory(String name, String type) {
        return categoryRepository.findByName(name)
                .map(existing -> {
                    if (type != null && !type.isEmpty() && !type.equals(existing.getType())) {
                        existing.setType(type);
                        return categoryRepository.save(existing);
                    }
                    return existing;
                })
                .orElseGet(() -> {
                    Category newCat = new Category();
                    newCat.setName(name);
                    newCat.setType(type != null && !type.isEmpty() ? type : "EXPENSE");
                    return categoryRepository.save(newCat);
                });
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .description(t.getDescription())
                .transactionDate(t.getTransactionDate())
                .categoryName(t.getCategory() != null ? t.getCategory().getName() : null)
                .categoryType(t.getCategory() != null ? t.getCategory().getType() : null)
                .build();
    }
}