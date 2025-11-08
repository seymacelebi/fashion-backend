package com.fashion.fashion_backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Uygulamanın genel yapılandırma bean'lerini (beans) içeren sınıf.
 * Spring Context'i başlarken bu sınıftaki metotları çalıştırarak
 * gerekli nesneleri oluşturur ve hafızaya alır.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    // DEĞİŞİKLİK 1: UserRepository bağımlılığı kaldırıldı, çünkü bu mantık artık
    // UserDetailsServiceImpl sınıfı tarafından yönetiliyor.

    /**
     * AuthenticationManager bean'ini Spring Context'ine ekler.
     * AuthController gibi sınıflar bu bean'i kullanarak kimlik doğrulama işlemlerini yapabilir.
     * @param config Spring tarafından otomatik olarak sağlanan AuthenticationConfiguration.
     * @return AuthenticationManager nesnesi.
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Şifrelerin güvenli bir şekilde şifrelenmesi (hashing) için PasswordEncoder bean'ini tanımlar.
     * BCrypt, güçlü ve standart bir şifreleme algoritmasıdır.
     * @return BCryptPasswordEncoder nesnesi.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // DEĞİŞİKLİK 2: userDetailsService() bean'i buradan kaldırıldı.
    // Spring artık bu bean'i @Service anotasyonuna sahip UserDetailsServiceImpl sınıfından
    // otomatik olarak alacak. Bu, bean çakışmasını önler.

    /**
     * Kimlik doğrulama sağlayıcısını (AuthenticationProvider) tanımlar.
     * Spring Security'ye, kullanıcıları doğrulamak için UserDetailsService'i ve şifreleri
     * kontrol etmek için PasswordEncoder'ı kullanmasını söyler.
     * @param userDetailsService Spring'in otomatik olarak bulacağı UserDetailsServiceImpl nesnesi.
     * @return AuthenticationProvider nesnesi.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        // DEĞİŞİKLİK 3: Spring, UserDetailsServiceImpl'i bu metoda otomatik olarak enjekte edecek.
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}

