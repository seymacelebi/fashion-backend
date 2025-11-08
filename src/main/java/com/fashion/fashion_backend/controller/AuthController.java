package com.fashion.fashion_backend.controller;

// Gerekli DTO'ları ve Servisi import ediyoruz
import static com.fashion.fashion_backend.entity.dto.AuthDTOs.AuthResponseDto;
import static com.fashion.fashion_backend.entity.dto.AuthDTOs.LoginRequestDto;
import static com.fashion.fashion_backend.entity.dto.AuthDTOs.RegisterRequestDto;
import com.fashion.fashion_backend.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Bu sınıfın bir REST API Controller'ı olduğunu belirtir
@RequestMapping("/api/auth") // Bu Controller'daki tüm yolların "/api/auth" ile başlayacağını belirtir
public class AuthController {

    private final AuthService authService; // "Güvenlik Müdürü" olan Şef'e bağlanır

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Yeni kullanıcı kayıt endpoint'i.
     * POST isteği ile /api/auth/register adresine gelir.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Valid @RequestBody RegisterRequestDto request // Gelen isteğin 'body' kısmını DTO'ya çevir
    ) {
        // Siparişi (request) Şef'e (authService) ilet
        AuthResponseDto response = authService.register(request);
        // Müşteriye (Frontend) "Başarıyla oluşturuldu" (201) yanıtı ve Token'ı dön
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Kullanıcı giriş endpoint'i.
     * POST isteği ile /api/auth/login adresine gelir.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        // Siparişi (request) Şef'e (authService) ilet
        AuthResponseDto response = authService.login(request);
        // Müşteriye (Frontend) "Başarılı" (200 OK) yanıtı ve Token'ı dön
        return ResponseEntity.ok(response);
    }
}

