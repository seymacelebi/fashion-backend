package com.fashion.fashion_backend.entity.dto;

/**
 * Product (Giysi) ile ilgili tüm DTO'ları (Veri Aktarım Nesneleri)
 * bir arada gruplayan ana sınıf.
 */
public class ProductDTOs {

    /**
     * Bir ürün (giysi) oluşturulurken Controller'dan Service'e gönderilecek veriyi temsil eder.
     * Bu, ProductDTOs sınıfı içine yerleştirilmiş bir "static nested record" olarak tanımlanmıştır.
     * Diğer sınıflardan kullanımı: new ProductDTOs.ProductCreateDto(...)
     *
     * @param name Giysinin adı (örn: "Mavi Kot Ceket")
     * @param imageUrl Giysinin resim URL'i
     * @param categoryId Giysinin ait olduğu kategorinin ID'si (örn: 1L -> "Ceketler")
     */
    public static record ProductCreateDto(
            String name,
            String imageUrl,
            Long categoryId
    ) {
    }

    /**
     * Bir ürün (giysi) bilgisi Service'ten Controller'a geri döndürülürken kullanılır.
     * Bu, ProductDTOs sınıfı içine yerleştirilmiş bir "static nested record" olarak tanımlanmıştır.
     * Diğer sınıflardan kullanımı: new ProductDTOs.ProductDto(...)
     *
     * @param id Giysinin ID'si
     * @param name Giysinin adı
     * @param imageUrl Giysinin resim URL'i
     * @param categoryName Giysinin ait olduğu kategorinin adı (örn: "Ceketler")
     * @param userId Giysinin sahibinin ID'si
     */
    public static record ProductDto(
            Long id,
            String name,
            String imageUrl,
            String categoryName,
            Long userId
    ) {
    }

}
