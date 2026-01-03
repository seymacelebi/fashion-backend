package com.fashion.fashion_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "collaboration_products")
@Getter
@Setter
@NoArgsConstructor
public class CollaborationProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ürün adı

    private String imageUrl; // Ürün görseli

    private String productUrl; // Ürünün satın alma linki (Affiliate link)

    private BigDecimal price; // Fiyat bilgisi (Opsiyonel)

    // ÇOK Ürün, BİR Markaya aittir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;
}