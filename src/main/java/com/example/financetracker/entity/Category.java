package com.example.financetracker.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories") // Ánh xạ với bảng 'categories' trong SQL
@Data // Lombok tự động sinh Getter, Setter, Constructor...
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // 'INCOME' hoặc 'EXPENSE'
}