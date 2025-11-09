package com.fashion.fashion_backend.config; // Kendi config paketinizin adı

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider; // Bunu zaten ApplicationConfig'de tanımladınız
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // !! 1. KRİTİK NOKTA !!
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize'un çalışması için bu ŞART!
@RequiredArgsConstructor
public class SecurityConfig {

    // Kendi JwtAuthenticationFilter sınıfınız (bir önceki adımda göstermiştiniz)
    private final JwtAuthenticationFilter jwtAuthFilter;

    // ApplicationConfig'de oluşturduğunuz AuthenticationProvider bean'i
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // REST API'ler için standart

                // !! 2. KRİTİK NOKTA: İSTEK KURALLARI !!
                .authorizeHttpRequests(auth -> auth
                        // BU YOLLARA (Giriş, Kayıt, Swagger) KİMLİK DOĞRULAMASI OLMADAN ERİŞİLEBİLSİN
                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // YUKARIDAKİLER DIŞINDAKİ TÜM İSTEKLER...
                        .anyRequest()
                        .authenticated() // ...KİMLİK DOĞRULAMASI GEREKTİRSİN
                )

                // JWT kullandığımız için oturum (Session) STATELESS olmalı
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Spring'e hangi AuthenticationProvider'ı kullanacağını söylüyoruz
                .authenticationProvider(authenticationProvider)

                // Sizin JWT filtrenizi, standart filtreden ÖNCE çalıştır
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}