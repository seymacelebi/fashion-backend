package com.fashion.fashion_backend.repository;

import com.fashion.fashion_backend.entity.Product;
import java.util.List;

import com.fashion.fashion_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Kullanıcının tüm ürünlerini bul
    List<Product> findByUser(User user);

	  /**
     * Belirli bir kullanıcı ID'sine ait tüm ürünleri (giysileri) bulur.
     * Metot isimlendirmesi sayesinde Spring Data JPA bu sorguyu otomatik oluşturur.
     * SQL: "SELECT * FROM products WHERE user_id = ?"
     *
     * @param userId Giysileri listelenecek kullanıcının ID'si
     * @return O kullanıcıya ait Product listesi
     */
    List<Product> findAllByUserId(Long userId);


    /**
     * Belirli bir kullanıcıya ve belirli bir kategoriye ait tüm ürünleri bulur.
     * SQL: "SELECT * FROM products WHERE user_id = ? AND category_id = ?"
     *
     * @param userId     Kullanıcı ID'si
     * @param categoryId Kategori ID'si
     * @return O kullanıcı ve kategoriye ait Product listesi
     */
    List<Product> findAllByUserIdAndCategoryId(Long userId, Long categoryId);

    // Kategori silerken kontrol için
    boolean existsByCategoryId(Long categoryId);
}