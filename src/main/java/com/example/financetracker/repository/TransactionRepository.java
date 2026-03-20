package com.example.financetracker.repository;

import com.example.financetracker.entity.Transaction;
import com.example.financetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Lấy tất cả theo user
    List<Transaction> findByUser(User user);

    // Tổng theo loại của user
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.category.type = :type AND t.user = :user")
    Double sumTotalByTypeAndUser(@Param("type") String type, @Param("user") User user);

    // Lọc kết hợp theo user
    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND " +
            "(:type IS NULL OR t.category.type = :type) AND " +
            "(:from IS NULL OR t.transactionDate >= :from) AND " +
            "(:to IS NULL OR t.transactionDate <= :to) " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findWithFilters(
            @Param("user") User user,
            @Param("type") String type,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    // Tìm theo id và user (tránh xem của người khác)
    Optional<Transaction> findByIdAndUser(Long id, User user);
}