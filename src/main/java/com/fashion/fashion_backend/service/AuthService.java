package com.fashion.fashion_backend.service;

import static com.fashion.fashion_backend.entity.dto.AuthDTOs.AuthResponseDto;
import static com.fashion.fashion_backend.entity.dto.AuthDTOs.LoginRequestDto;
import static com.fashion.fashion_backend.entity.dto.AuthDTOs.RegisterRequestDto;

/**
 * Kullanıcı kayıt (registration) ve giriş (login) işlemleri için
 * iş mantığı katmanının sözleşmesi.
 */
public interface AuthService {

    /**
     * Yeni bir kullanıcıyı sisteme kaydeder.
     * @param request Kayıt için gereken bilgileri (username, email, password) içeren DTO.
     * @return Başarılı kimlik doğrulama yanıtı (JWT Token ve kullanıcı bilgileri).
     */
    AuthResponseDto register(RegisterRequestDto request);

    /**
     * Mevcut bir kullanıcının sisteme giriş yapmasını sağlar.
     * @param request Giriş için gereken bilgileri (email, password) içeren DTO.
     * @return Başarılı kimlik doğrulama yanıtı (JWT Token ve kullanıcı bilgileri).
     */
    AuthResponseDto login(LoginRequestDto request);
}
