package com.fashion.fashion_backend.service;

import com.fashion.fashion_backend.entity.dto.UserDTOs.UserProfileDto;

import java.util.List;

/**
 * Kimlik doğrulama DIŞINDAKİ kullanıcı yönetimi işlemleri için
 * iş mantığı katmanının sözleşmesi. (örn: profil işlemleri, admin işlemleri)
 */
public interface UserService {

    /**
     * O anda sistemde kimliği doğrulanmış (giriş yapmış) olan kullanıcının
     * profil bilgilerini getirir.
     *
     * @return Giriş yapmış kullanıcının UserProfileDto'su.
     */
    UserProfileDto getCurrentUserProfile();

    /**
     * [ADMIN YETKİSİ GEREKTİRİR]
     * Sistemdeki tüm kullanıcıların listesini getirir.
     *
     * @return Tüm kullanıcıların UserProfileDto listesi.
     */
    List<UserProfileDto> getAllUsers();
}
