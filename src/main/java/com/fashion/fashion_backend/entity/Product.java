package com.fashion.fashion_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data; // Bunu import etmeyi unutma
import jakarta.persistence.*;

import lombok.Getter;       // <-- BUNU EKLEYİN
import lombok.Setter;      // <-- BUNU EKLEYİN
import lombok.NoArgsConstructor; // <-- BUNU EKLEYİN

@Entity
@Table(name = "products")
@Getter
@Setter
@Data  // <--- BU ANOTASYON GETTER'LARI OTOMATİK OLUŞTURUR
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Örn: "Mavi Kot Ceket"

    private String imageUrl;

    // --- YENİ EKLENEN ALANLAR ---

    // 1. Renk (Düz yazı olarak)
    @Column(name = "color")
    private String color; // Örn: "Haki", "Kırmızı"

    // 2. Sezon (Enum - Veritabanına 'SUMMER', 'WINTER' diye yazı olarak yazar)
    @Enumerated(EnumType.STRING)
    @Column(name = "season")
    private Season season;

    // 3. Stil (Enum - Veritabanına 'CASUAL', 'OFFICE' diye yazı olarak yazar)
    @Enumerated(EnumType.STRING)
    @Column(name = "style")
    private Style style;

    @ManyToOne(fetch = FetchType.EAGER) // EAGER: Ürünü çekerken markayı da getirir.
    @JoinColumn(name = "brand_id", nullable = true) // Veritabanında 'brand_id' kolonu oluşur.
    private Brand brand;

    private Long price;

    // ----------------------------

    // ÇOK Giysi, BİR Kullanıcıya aittir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ÇOK Giysi, BİR Kategoriye aittir.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}