package com.fashion.fashion_backend.service;

import com.fashion.fashion_backend.entity.Category;
import com.fashion.fashion_backend.entity.Product;
import com.fashion.fashion_backend.entity.User;
import com.fashion.fashion_backend.entity.dto.ProductDTOs.ProductCreateDto;
import com.fashion.fashion_backend.entity.dto.ProductDTOs.ProductDto;
import com.fashion.fashion_backend.exception.ResourceNotFoundException;
import com.fashion.fashion_backend.repository.CategoryRepository;
import com.fashion.fashion_backend.repository.ProductRepository;
import com.fashion.fashion_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException; // veya kendi özel exception'ınız
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- YENİ IMPORT: Transaction yönetimi için

import java.util.List;
import java.util.stream.Collectors;

/**
 * ProductService arayüzünün (interface) somut uygulaması.
 * Gerçek iş mantığı burada yer alır.
 */
@Service
public class ProductServiceImpl implements ProductService {

    // Gerekli olan tüm repository'leri enjekte ediyoruz.
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional // <-- YENİ EKLENDİ (Yazma işlemi)
    public ProductDto createProduct(ProductCreateDto createDto, Long userId) {
        // 1. İş Mantığı: Giysiyi ekleyen kullanıcı var mı?
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // 2. İş Mantığı: Giysinin ekleneceği kategori var mı?
        Category category = categoryRepository.findById(createDto.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + createDto.categoryId()));

        // 3. Yeni Product (Giysi) entity'sini oluştur
        Product newProduct = new Product();
        newProduct.setName(createDto.name());
        newProduct.setImageUrl(createDto.imageUrl());
        newProduct.setUser(user); // İlişkiyi kur (Foreign Key)
        newProduct.setCategory(category); // İlişkiyi kur (Foreign Key)

        // 4. Veritabanına kaydet
        Product savedProduct = productRepository.save(newProduct);

        // 5. Kaydedilen entity'yi DTO'ya çevirip Controller'a dön
        return mapToDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true) // <-- YENİ EKLENDİ (Okuma işlemi)
    public ProductDto getProductById(Long productId, Long userId) {
        // 1. Veritabanından ürünü bul
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // 2. Güvenlik Kontrolü: Bu ürünü isteyen kişi, ürünün sahibi mi?
        if (!product.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to access this product.");
        }

        // 3. DTO'ya çevirip dön
        return mapToDto(product);
    }

    @Override
    @Transactional(readOnly = true) // <-- YENİ EKLENDİ (Okuma işlemi)
    public List<ProductDto> getAllProductsByUserId(Long userId) {
        // 1. Repository'den o kullanıcıya ait tüm ürünleri çek
        List<Product> products = productRepository.findAllByUserId(userId);

        // 2. Bu listeyi DTO listesine çevir (stream kullanarak)
        return products.stream()
                .map(this::mapToDto) // Her bir product için mapToDto metodunu çağır
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true) // <-- YENİ EKLENDİ (Okuma işlemi)
    public List<ProductDto> getProductsByUserIdAndCategory(Long userId, Long categoryId) {
        // 1. Repository'den filtrelenmiş veriyi çek
        List<Product> products = productRepository.findAllByUserIdAndCategoryId(userId, categoryId);
        
        // 2. DTO listesine çevir
        return products.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional // <-- YENİ EKLENDİ (Silme işlemi)
    public void deleteProduct(Long productId, Long userId) {
        // 1. Veritabanından ürünü bul
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // 2. Güvenlik Kontrolü: Bu ürünü silmek isteyen kişi, ürünün sahibi mi?
        if (!product.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this product.");
        }

        // 3. Ürünü sil
        productRepository.delete(product);
    }


    // === YARDIMCI METOT (HELPER METHOD) ===

    /**
     * Product entity'sini ProductDto'ya dönüştüren özel bir yardımcı metot.
     * Kod tekrarını engeller.
     */
    private ProductDto mapToDto(Product product) {
        // NullPointerException'a karşı daha güvenli (defansif) kod.
        String categoryName = (product.getCategory() != null) ? product.getCategory().getName() : null;
        Long userId = (product.getUser() != null) ? product.getUser().getId() : null;

        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                categoryName, // Kategori adını ilişkiden güvenle çek
                userId // Kullanıcı ID'sini ilişkiden güvenle çek
        );
    }
}

