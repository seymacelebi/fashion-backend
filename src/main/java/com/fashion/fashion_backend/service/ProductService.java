package com.fashion.fashion_backend.service;

// DTO'ları "static import" ile doğrudan içeri alıyoruz.
// Bu sayede ProductDTOs.ProductDto yazmak yerine sadece ProductDto yazabiliriz.
// Bu, kodun geri kalanını değiştirmeden sadece import kısmını güncellememizi sağlar.
import static com.fashion.fashion_backend.entity.dto.ProductDTOs.ProductCreateDto;
import static com.fashion.fashion_backend.entity.dto.ProductDTOs.ProductDto;

import java.util.List;

/**
 * Product (Giysi) işlemleri için iş mantığı katmanının (Business Logic Layer)
 * sözleşmesi (contract).
 */
public interface ProductService {

    /**
     * Belirli bir kullanıcı için yeni bir giysi oluşturur.
     *
     * @param createDto Yeni giysinin bilgilerini (adı, resmi, kategori ID'si) içeren DTO.
     * @param userId    Giysiyi oluşturan kullanıcının ID'si (Genellikle JWT'den alınır).
     * @return Oluşturulan giysinin bilgilerini içeren ProductDto.
     */
    ProductDto createProduct(ProductCreateDto createDto, Long userId);

    /**
     * Belirli bir ID'ye sahip giysiyi, sahibinin ID'si ile birlikte getirir.
     *
     * @param productId Giysinin ID'si.
     * @param userId    Giysinin sahibi olan kullanıcının ID'si (Güvenlik kontrolü için).
     * @return Bulunan giysinin bilgileri.
     */
    ProductDto getProductById(Long productId, Long userId);

    /**
     * Belirli bir kullanıcının tüm giysilerini listeler.
     *
     * @param userId Giysileri listelenecek kullanıcının ID'si.
     * @return O kullanıcıya ait giysilerin DTO listesi.
     */
    List<ProductDto> getAllProductsByUserId(Long userId);

    /**
     * Belirli bir kullanıcının belirli bir kategorideki tüm giysilerini listeler.
     *
     * @param userId     Kullanıcı ID'si.
     * @param categoryId Kategori ID'si.
     * @return Filtrelenmiş giysi DTO listesi.
     */
    List<ProductDto> getProductsByUserIdAndCategory(Long userId, Long categoryId);

    /**
     * Bir giysiyi siler.
     *
     * @param productId Silinecek giysinin ID'si.
     * @param userId    Giysiyi silmeye çalışan kullanıcının ID'si (Güvenlik kontrolü için).
     */
    void deleteProduct(Long productId, Long userId);

    // Not: updateProduct metodu da benzer bir mantıkla eklenebilir.
}

