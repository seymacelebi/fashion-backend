package com.fashion.fashion_backend.entity.dto;

/**
 * Kullanıcı yönetimi (CRUD) işlemleri için gereken DTO'ları gruplar.
 * Bu DTO'lar, 'AuthDTOs'den farklıdır, token veya şifre içermezler.
 */
public class UserDTOs {

    /**
     * Bir kullanıcının profil bilgilerini (token veya şifre olmadan)
     * güvenli bir şekilde dış dünyaya (frontend'e) döndürmek için kullanılır.
     *
     * @param id       Kullanıcı ID'si
     * @param username Kullanıcının adı
     * @param email    Kullanıcının email adresi
     */
    public static record UserProfileDto(
            Long id,
            String username,
            String email
    ) {
    }

    // Gelecekte bir kullanıcının profilini güncellemek için
    // public static record UserUpdateDto( ... ) de buraya eklenebilir.
}
