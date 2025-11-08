package com.fashion.fashion_backend.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "combinations") // 'stilrehberi.combinations'
public class Combination {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name; // Örn: "Hafta Sonu Kombini"
    
    // ÇOK Kombin, BİR Kullanıcıya aittir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // ÇOK Kombin, ÇOK Giysiden oluşur (Many-to-Many).
    // Hibernate'in sihirli kısmı budur:
    // Bu anotasyon, 'stilrehberi.combination_products' adında
    // iki sütunlu (combination_id, product_id) bir "ilişki tablosu"nu
    // OTOMATİK olarak oluşturacaktır.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "combination_products",
        joinColumns = @JoinColumn(name = "combination_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products;
    
    // ... (Getters, Setters, Constructors) ...
}