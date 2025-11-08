package com.fashion.fashion_backend.service;

import static com.fashion.fashion_backend.entity.dto.CategoryDTOs.CategoryCreateDto;
import static com.fashion.fashion_backend.entity.dto.CategoryDTOs.CategoryDto;

import java.util.List;

/**
 * Category (Kategori) işlemleri için iş mantığı katmanının (Business Logic Layer)
 * sözleşmesi (contract).
 */
public interface CategoryService {

    /**
     * Tüm kategorileri listeler.
     * @return Kategori DTO'larının bir listesi.
     */
    List<CategoryDto> getAllCategories();

    /**
     * Yeni bir kategori oluşturur.
     * @param createDto Yeni kategorinin bilgilerini içeren DTO.
     * @return Oluşturulan kategorinin DTO'su.
     */
    CategoryDto createCategory(CategoryCreateDto createDto);

    /**
     * Bir kategoriyi ID'sine göre siler.
     * @param categoryId Silinecek kategorinin ID'si.
     */
    void deleteCategory(Long categoryId);
}
