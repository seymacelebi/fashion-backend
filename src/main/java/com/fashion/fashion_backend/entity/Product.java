package com.fashion.fashion_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

import lombok.Getter;       // <-- BUNU EKLEYİN
import lombok.Setter;      // <-- BUNU EKLEYİN
import lombok.NoArgsConstructor; // <-- BUNU EKLEYİN

@Entity
@Table(name = "products") // 'stilrehberi.products'
@Getter                 // <-- BU SATIRI EKLEYİN (Tüm alanlar için getter oluşturur)
@Setter                 // <-- BU SATIRI EKLEYİN (Tüm alanlar için setter oluşturur)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // <-- BU SATIRI EKLEYİN
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name; // Örn: "Mavi Kot Ceket"
    
    private String imageUrl;
    
    // ÇOK Giysi, BİR Kullanıcıya aittir.
    // Bu, 'users' tablosuna bir Foreign Key (user_id) oluşturur.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // ÇOK Giysi, BİR Kategoriye aittir.
    // Bu, 'categories' tablosuna bir Foreign Key (category_id) oluşturur.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    // ... (Getters, Setters, Constructors) ...
    // Not: @ManyToMany ilişkisi Combination sınıfı tarafından yönetilecek.
}