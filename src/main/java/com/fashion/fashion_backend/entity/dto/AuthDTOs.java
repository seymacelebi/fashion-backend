package com.fashion.fashion_backend.entity.dto;

/**
 * Authentication (Kimlik Doğrulama) işlemleri için gereken tüm DTO'ları gruplar.
 */
public class AuthDTOs {

    /**
     * Yeni bir kullanıcı kayıt olurken Controller'dan alınacak veriyi temsil eder.
     * @param username Kullanıcının benzersiz adı
     * @param email Kullanıcının giriş için kullanacağı email
     * @param password Kullanıcının şifresi
     */
    public static record RegisterRequestDto(
            String username,
            String email,
            String password
    ) {
    }

    /**
     * Kullanıcı giriş yaparken Controller'dan alınacak veriyi temsil eder.
     * @param email Kullanıcının email'i
     * @param password Kullanıcının şifresi
     */
    public static record LoginRequestDto(
            String email,
            String password
    ) {
    }

    /**
     * Başarılı bir kayıt veya giriş işleminden sonra Controller'dan
     * frontend'e geri döndürülecek veriyi temsil eder.
     * @param token JWT (JSON Web Token)
     * @param userId Kullanıcının ID'si
     * @param email Kullanıcının email'i
     * @param username Kullanıcının adı
     */
    public static record AuthResponseDto(
            String token,
            Long userId,
            String email,
            String username
    ) {
    }
}
