package com.fashion.fashion_backend.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "combinations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Combination {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name; // Örn: "Hafta Sonu Kombini"

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "combination_products",
        joinColumns = @JoinColumn(name = "combination_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}