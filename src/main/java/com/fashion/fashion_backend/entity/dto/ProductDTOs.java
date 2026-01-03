package com.fashion.fashion_backend.entity.dto;


import com.fashion.fashion_backend.entity.Brand;
import com.fashion.fashion_backend.entity.Season; // Enum'ları import etmeyi unutmayın
import com.fashion.fashion_backend.entity.Style;  // Enum'ları import etmeyi unutmayın
import org.springframework.web.multipart.MultipartFile;

/**
 * Product (Giysi) ile ilgili tüm DTO'ları gruplayan ana sınıf.
 */
public class ProductDTOs {

    /**
     * Ürün OLUŞTURMA (Request) DTO'su.
     * Frontend'den Backend'e veri gelirken kullanılır.
     */
    public static record ProductCreateDto(
            String name,
            MultipartFile image,
            Long categoryId,
            String color,   // Örn: "Haki"
            Season season,  // Örn: "SUMMER" (Frontend string olarak gönderir, Spring otomatik Enum'a çevirir)
            Style style  ,
            String brandName// Örn: "CASUAL"
    ) {
    }

    /**
     * Ürün LİSTELEME/GÖSTERME (Response) DTO'su.
     * Backend'den Frontend'e veri dönerken kullanılır.
     */
    public static record ProductDto(

             Long id,
            String name,
            String imageUrl,      // 🔥 STRING (Cloudinary URL)
            String categoryName,
            Long userId,
            String color,
            Season season,
            Style style,
            String brandName,
            Long price
    ) {
    }

}
