package com.fashion.fashion_backend.service;
import com.fashion.fashion_backend.entity.Combination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.fashion.fashion_backend.entity.Product;
import com.fashion.fashion_backend.entity.User;
import com.fashion.fashion_backend.entity.dto.CombinationRequest;
import com.fashion.fashion_backend.entity.dto.CombinationResponse;
import com.fashion.fashion_backend.repository.CombinationRepository;
import com.fashion.fashion_backend.repository.ProductRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CombinationServiceImpl implements CombinationService {
    private final CombinationRepository combinationRepository;
    private final ProductRepository productRepository;

    public CombinationServiceImpl(CombinationRepository combinationRepository, ProductRepository productRepository) {
        this.combinationRepository = combinationRepository;
        this.productRepository = productRepository;
    }


    @Override
    @Transactional
    public CombinationResponse createCombination(CombinationRequest request, User user) {
        List<Product> productList = productRepository.findAllById(request.getProductIds());

        if (productList.isEmpty()) {
            throw new RuntimeException("Kombin oluşturmak için seçilen ürünler bulunamadı.");
        }

        // 2. Güvenlik Kontrolü: Tüm ürünler giriş yapan kullanıcıya mı ait?
        boolean allOwnedByUser = productList.stream()
                .allMatch(p -> p.getUser().getId().equals(user.getId()));

        if (!allOwnedByUser) {
            throw new RuntimeException("Size ait olmayan ürünlerle kombin oluşturamazsınız!");
        }

        // 3. Mükerrer Kayıt Kontrolü (Business Logic)
        Set<Product> newProductSet = new HashSet<>(productList);
        List<Combination> existingCombinations = combinationRepository.findByUser(user);

        for (Combination existing : existingCombinations) {
            // A - İsim Kontrolü: Aynı isimde kombin var mı? (Case-insensitive)
            if (existing.getName().equalsIgnoreCase(request.getName().trim())) {
                throw new RuntimeException("'" + request.getName() + "' adında bir kombin zaten mevcut!");
            }

            // B - İçerik Kontrolü: Aynı kıyafetlerden oluşan başka bir kombin var mı?
            // Java Set equality (equals), iki setin aynı elemanlara sahip olup olmadığını kontrol eder.
            if (existing.getProducts().equals(newProductSet)) {
                throw new RuntimeException("Bu parçalardan oluşan bir kombin zaten mevcut (Kombin adı: " + existing.getName() + ")");
            }
        }

        // 4. Entity oluşturma
        Combination combination = Combination.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .products(new HashSet<>(productList))
                .build();

        Combination savedCombination = combinationRepository.save(combination);

        // 4. Response DTO'ya dönüştürüp döndürüyoruz
        return mapToResponse(savedCombination);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CombinationResponse> getUserCombinations(User user) {
        return combinationRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCombination(Long id, User user) {
        Combination combination = combinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kombin bulunamadı."));

        // Güvenlik Kontrolü: Kombin bu kullanıcıya mı ait?
        if (!combination.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu kombini silme yetkiniz yok!");
        }

        combinationRepository.delete(combination);
    }

    @Override
    @Transactional(readOnly = true)
    public CombinationResponse getCombinationById(Long id, User user) {
        Combination combination = combinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kombin bulunamadı."));

        if (!combination.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu kombini görüntüleme yetkiniz yok!");
        }

        return mapToResponse(combination);
    }

    /**
     * Entity -> Response DTO Dönüştürücü (Mapping)
     */
    private CombinationResponse mapToResponse(Combination combination) {
        List<CombinationResponse.ProductSummaryDTO> productSummaries = combination.getProducts().stream()
                .map(p -> CombinationResponse.ProductSummaryDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .imageUrl(p.getImageUrl())
                        .categoryName(p.getCategory() != null ? p.getCategory().getName() : "Genel")
                        .brandName(p.getBrand() != null ? p.getBrand().getName() : "Markasız")
                        .price(p.getPrice() != null ? p.getPrice() : 0.0)
                        .build())
                .collect(Collectors.toList());

        // Toplam değeri hesapla
        double total = combination.getProducts().stream()
                .mapToDouble(p -> p.getPrice() != null ? p.getPrice() : 0.0)
                .sum();

        return CombinationResponse.builder()
                .id(combination.getId())
                .name(combination.getName())
                .description(combination.getDescription())
                .createdAt(combination.getCreatedAt())
                .products(productSummaries)
                .totalValue(total)
                .build();
    }
}