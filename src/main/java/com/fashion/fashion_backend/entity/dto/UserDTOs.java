package com.fashion.fashion_backend.entity.dto;

/**
 * Kullanıcı yönetimi (CRUD) işlemleri için gereken DTO'ları gruplar.
 * Bu DTO'lar, 'AuthDTOs'den farklıdır, token veya şifre içermezler.
 */
public class UserDTOs {

    public static record UserProfileDto(
            Long id,
            String username,
            String email
    ) {
    }

}
