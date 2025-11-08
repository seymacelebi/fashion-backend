package com.fashion.fashion_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "categories")
@Getter              
@Setter                 
@JsonIgnoreProperties(ignoreUnknown = true) // <-- BU SATIRI EKLEYİN

public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name; // Örn: "Ceket", "Pantolon", "Ayakkabı"
    
    // BİR Kategorinin, ÇOK Giysisi olabilir.
    @OneToMany(mappedBy = "category")
    private Set<Product> products;
    
    // ... (Getters, Setters, Constructors) ...
}