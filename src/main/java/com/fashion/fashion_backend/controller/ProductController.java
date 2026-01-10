package com.fashion.fashion_backend.controller;

import static com.fashion.fashion_backend.entity.dto.ProductDTOs.ProductCreateDto;
import static com.fashion.fashion_backend.entity.dto.ProductDTOs.ProductDto;
import com.fashion.fashion_backend.service.ProductService;
import com.fashion.fashion_backend.entity.User; // GÜNCELLEME: User entity'sini import ediyoruz.

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // GÜNCELLEME: Güvenlik importları
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Giysi (Product) işlemleri için REST API endpoint'lerini yönetir.
 * Bu "Garson"dur. Güvenlikten (SecurityContext) kullanıcıyı alır ve "Şef"e (ProductService) iletir.
 */
@RestController
@RequestMapping("/api/products") // Bu Controller'daki tüm yollar "/api/products" ile başlar
public class ProductController {

    private final ProductService productService; // "Giysi" Şef'ine bağlanır

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> createProduct(
            @ModelAttribute ProductCreateDto createDto
    ) {
        System.out.println("IMAGE NULL MI? " + (createDto.image() == null));
        System.out.println("IMAGE NAME: " +
                (createDto.image() != null ? createDto.image().getOriginalFilename() : "NULL"));

        Long currentUserId = getCurrentUserId();

        ProductDto newProduct =
                productService.createProduct(createDto, currentUserId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newProduct);
    }


    /**
     * O an giriş yapmış kullanıcının giysilerini listeler.
     * GET isteği ile /api/products adresine gelir.
     * * GÜNCELLEME: Bu metot artık Kategori ID'sine göre filtrelemeyi de destekler.
     * Örn: /api/products -> Tüm ürünleri getirir
     * Örn: /api/products?categoryId=2 -> Sadece 2 numaralı kategorideki ürünleri getirir
     */
    @GetMapping
    public ResponseEntity<List<ProductDto>> getMyProducts(
            @RequestParam(name = "categoryId", required = false) Long categoryId
    ) {
        Long currentUserId = getCurrentUserId();
        List<ProductDto> products;

        if (categoryId != null) {
            // Kategoriye göre filtrele
            products = productService.getProductsByUserIdAndCategory(currentUserId, categoryId);
        } else {
            // Tüm ürünleri getir
            products = productService.getAllProductsByUserId(currentUserId);
        }
        
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @ModelAttribute ProductCreateDto updateDto
    ) {
        System.out.println("UPDATE PRODUCT ID: " + id);
        System.out.println("IMAGE NULL MI? " + (updateDto.image() == null));
        System.out.println("IMAGE NAME: " +
                (updateDto.image() != null
                        ? updateDto.image().getOriginalFilename()
                        : "NULL"));

        Long currentUserId = getCurrentUserId();

        ProductDto updatedProduct =
                productService.updateProduct(id, updateDto, currentUserId);

        return ResponseEntity.ok(updatedProduct);
    }


    /**
     * Belirli bir ID'ye sahip giysiyi getirir.
     * GET isteği ile /api/products/{id} adresine gelir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(
            @PathVariable("id") Long productId
    ) {
        Long currentUserId = getCurrentUserId();
        // Şef'e (productService) hem ürün ID'sini hem de kullanıcı ID'sini ver.
        // Şef, bu ürünün bu kullanıcıya ait olup olmadığını kontrol edecektir.
        ProductDto product = productService.getProductById(productId, currentUserId);
        return ResponseEntity.ok(product);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("id") Long productId
    ) {
        // GÜNCELLEME: Şef'e (productService) silme emrini verirken
        // güvenliğin sağlanması için kullanıcı ID'sini de iletiyoruz.
        Long currentUserId = getCurrentUserId();
        productService.deleteProduct(productId, currentUserId);
        
        // "İçerik yok" (204) yanıtı dön
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {
            // Bu durum normalde JwtAuthenticationFilter tarafından engellenir,
            // ancak yine de bir güvenlik önlemidir.
            throw new SecurityException("User is not authenticated or principal is not of type User");
        }
        
        User currentUser = (User) authentication.getPrincipal();
        return currentUser.getId();
    }
}

