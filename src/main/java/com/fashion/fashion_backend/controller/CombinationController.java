package com.fashion.fashion_backend.controller;

import com.fashion.fashion_backend.entity.User;
import com.fashion.fashion_backend.entity.dto.CombinationRequest;
import com.fashion.fashion_backend.entity.dto.CombinationResponse;
import com.fashion.fashion_backend.service.CombinationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/combinations")
@RequiredArgsConstructor
public class CombinationController {

    private final CombinationService combinationService;

    /**
     * Yeni bir kombin oluşturur.
     * @param request Kombin adı, açıklaması ve ürün ID listesi.
     * @param user    JWT üzerinden çözümlenen aktif kullanıcı.
     * @return        Oluşturulan kombinin Response DTO hali ve 201 Created kodu.
     */
    @PostMapping
    public ResponseEntity<CombinationResponse> create(
            @Valid @RequestBody CombinationRequest request,
            @AuthenticationPrincipal User user
    ) {
        // Yeni bir kaynak oluşturulduğu için HTTP 201 Created dönmek standartlara uygundur.
        CombinationResponse created = combinationService.createCombination(request, user);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Oturum açmış kullanıcının oluşturduğu tüm kombinleri listeler.
     * @param user JWT üzerinden çözümlenen aktif kullanıcı.
     * @return     Kullanıcıya özel kombin listesi.
     */
    @GetMapping
    public ResponseEntity<List<CombinationResponse>> getAll(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(combinationService.getUserCombinations(user));
    }

    /**
     * Belirli bir kombinin detaylarını getirir.
     * @param id   Kombin ID.
     * @param user Güvenlik kontrolü için aktif kullanıcı.
     * @return     Kombin detayları.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CombinationResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(combinationService.getCombinationById(id, user));
    }

    /**
     * Bir kombini sistemden siler.
     * @param id   Silinecek kombinin ID'si.
     * @param user Sadece kombinin sahibi silebilmesi için aktif kullanıcı.
     * @return     204 No Content döner.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        combinationService.deleteCombination(id, user);
        return ResponseEntity.noContent().build();
    }
}