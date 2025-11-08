package com.fashion.fashion_backend.config;
//... diğer importlar ...
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults; // Bu importu ekleyin

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

 @Bean
 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
     http
         // YENİ EKLENEN SATIR: CORS ayarlarını güvenlik zincirine dahil et.
         .cors(withDefaults()) 
         .csrf(csrf -> csrf.disable())
         .authorizeHttpRequests(auth -> auth
             .requestMatchers("/**").permitAll() // Şimdilik tüm yollara izin verelim
             .anyRequest().authenticated()
         );

     return http.build();
 }

 // YENİ EKLENEN METOT: CORS ayarlarını merkezi olarak tanımlar.
 @Bean
 CorsConfigurationSource corsConfigurationSource() {
     CorsConfiguration configuration = new CorsConfiguration();
     // React uygulamanızın adresine izin verin:
     configuration.setAllowedOrigins(List.of("http://localhost:5173")); 
     // İzin verilen HTTP metotları:
     configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); 
     // İzin verilen başlıklar:
     configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); 
     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
     source.registerCorsConfiguration("/**", configuration); // Tüm API yolları için bu ayarları geçerli kıl
     return source;
 }

 // ... diğer bean'leriniz (PasswordEncoder vb.) ...
}