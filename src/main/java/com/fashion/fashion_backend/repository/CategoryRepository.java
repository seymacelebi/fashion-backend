package com.fashion.fashion_backend.repository;

import com.fashion.fashion_backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Kategori adını kullanarak bir kategoriyi bulur.
     * Yeni kategori oluştururken 'Bu isim zaten alınmış mı?' kontrolü için kullanışlıdır.
     * @param name Aranacak kategori adı
     * @return Kategori (varsa)
     */
    Optional<Category> findByName(String name);
}
