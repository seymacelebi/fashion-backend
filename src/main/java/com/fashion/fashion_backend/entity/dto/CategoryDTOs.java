package com.fashion.fashion_backend.entity.dto;

/**
 * Category (Kategori) ile ilgili tüm DTO'ları gruplayan ana sınıf.
 */
public class CategoryDTOs {

    /**
     * Yeni bir kategori oluştururken Controller'dan Service'e gönderilecek veriyi temsil eder.
     * @param name Kategorinin adı (örn: "Ceketler")
     */
    public static record CategoryCreateDto(
            String name
    ) {
    }

    /**
     * Bir kategori bilgisi Service'ten Controller'a geri döndürülürken kullanılır.
     * @param id Kategorinin ID'si
     * @param name Kategorinin adı
     */
    public static record CategoryDto(
            Long id,
            String name
    ) {
    }
}
