package com.fashion.fashion_backend.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // Örn: "Trendyol", "Zara"

    @Column(name = "logo_url")
    private String logoUrl; // Markanın logo görsel linki

    @Column(name = "affiliate_link_base")
    private String affiliateLinkBase; // Örn: "https://ty.gl/..." (Ortaklık linki kökü)

    // BİR Markanın, ÇOK İşbirliği Ürünü olabilir.
    // "mappedBy", CollaborationProduct sınıfındaki "brand" alanını işaret eder.
    // CascadeType.ALL: Marka silinirse, ona ait işbirliği ürünleri de silinsin.
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore // Sonsuz döngüye girmemesi için ürün listesini JSON'da gösterme (isteğe bağlı)
    private List<CollaborationProduct> collaborationProducts;

    // BİR Markanın, ÇOK normal Ürünü olabilir.
    // 'mappedBy', Product sınıfındaki 'brand' değişkeninin adıdır.
    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY)
    @JsonIgnore // JSON döngüsüne girmemesi için bu listeyi gizliyoruz
    private List<Product> products;
}