package com.fashion.fashion_backend.controller;

import static com.fashion.fashion_backend.entity.dto.CategoryDTOs.CategoryCreateDto;
import static com.fashion.fashion_backend.entity.dto.CategoryDTOs.CategoryDto;
import com.fashion.fashion_backend.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // ADMIN yetkilendirmesi için
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Kategori varlıkları için REST API endpoint'lerini yönetir.
 * Bu endpoint'lerin çoğu, sistem geneli verileri değiştirdiği için
 * ADMIN yetkisi gerektirir.
 */
@RestController
@RequestMapping("/api/categories") // Bu Controller'daki tüm yollar "/api/categories" ile başlar
public class CategoryController {

    private final CategoryService categoryService; // "Kategori" Şef'ine bağlanır

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Yeni bir kategori oluşturur.
     * Sadece ADMIN rolüne sahip kullanıcılar erişebilir.
     * POST isteği ile /api/categories adresine gelir.
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Bu endpoint'e sadece ADMIN erişebilir
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryCreateDto createDto
    ) {
        // Siparişi (createDto) Şef'e (categoryService) ilet
        CategoryDto newCategory = categoryService.createCategory(createDto);
        // "Başarıyla oluşturuldu" (201) yanıtı ile yeni kategoriyi dön
        return ResponseEntity.status(HttpStatus.CREATED).body(newCategory);
    }

    /**
     * Tüm kategorileri listeler.
     * GET isteği ile /api/categories adresine gelir.
     * Bu, tüm giriş yapmış (authenticated) kullanıcılar tarafından erişilebilir olmalı
     * (örneğin frontend'de bir ürün eklerken kategori seçtirmek için).
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        // Şef'ten (categoryService) tüm kategorilerin listesini iste
        List<CategoryDto> categories = categoryService.getAllCategories();
        // Listeyi (200 OK) yanıtı ile dön
        return ResponseEntity.ok(categories);
    }

    /**
     * Bir kategoriyi siler.
     * Sadece ADMIN rolüne sahip kullanıcılar erişebilir.
     * DELETE isteği ile /api/categories/{id} adresine gelir.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Bu endpoint'e sadece ADMIN erişebilir
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {
        // Şef'e (categoryService) silme emrini ver
        // Service katmanı (beyin), silmeden önce bu kategoriyi kullanan
        // bir ürün olup olmadığını zaten kontrol edecektir.
        categoryService.deleteCategory(id);
        // "İçerik yok" (204) yanıtı dön
        return ResponseEntity.noContent().build();
    }
}

