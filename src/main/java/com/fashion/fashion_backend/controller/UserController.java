package com.fashion.fashion_backend.controller;

// Gerekli DTO'ları ve Servisi statik olarak import ediyoruz
import static com.fashion.fashion_backend.entity.dto.UserDTOs.UserProfileDto;
import com.fashion.fashion_backend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users") // Bu Controller'daki tüm yollar "/api/users" ile başlar
public class UserController {

    private final UserService userService; // "Personel İşleri" Şef'ine bağlanır

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Giriş yapmış kullanıcının kendi profil bilgilerini getiren endpoint.
     * GET isteği ile /api/users/me adresine gelir.
     * Bu yol, SecurityConfig tarafından korunduğu için buraya sadece
     * geçerli bir token'a sahip kullanıcılar erişebilir.
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUser() {
        // Şef'ten (userService) o anki kullanıcının profilini iste
        UserProfileDto userProfile = userService.getCurrentUserProfile();
        // Müşteriye (Frontend) profili (200 OK) yanıtı ile dön
        return ResponseEntity.ok(userProfile);
    }

    /**
     * [ADMIN ÖZEL] Tüm kullanıcıları listeleyen endpoint.
     * GET isteği ile /api/users adresine gelir.
     * Bu metodun kendisi @PreAuthorize("hasRole('ADMIN')") ile korunduğu için
     * (UserServiceImpl'de) buraya sadece ADMIN rolündeki kullanıcılar erişebilir.
     * Not: @PreAuthorize serviste olduğu için buraya tekrar eklemek zorunlu değil,
     * ama güvenlik açısından controller'da da olması iyidir.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        // Şef'ten (userService) tüm kullanıcıların listesini iste
        List<UserProfileDto> users = userService.getAllUsers();
        // Listeyi (200 OK) yanıtı ile dön
        return ResponseEntity.ok(users);
    }
}

