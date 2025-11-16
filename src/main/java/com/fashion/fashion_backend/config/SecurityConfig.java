package com.fashion.fashion_backend.config; // Kendi config paketinizin adı

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import java.util.Arrays; // <-- YENİ İMPORT (Arrays.asList için)

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize'un çalışması için bu ŞART!
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF, HTTP Basic, Form Login ve Logout'u kapatıyoruz
                // REST API için bunların hiçbirine ihtiyacımız yok
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // CORS ayarlarını etkinleştir
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        // !! GÜNCELLEME: Login POST isteğini özel olarak listeye ekliyoruz !!
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()

                        // Diğer herkese açık yollar
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // YUKARIDAKİLER DIŞINDAKİ TÜM İSTEKLER...
                        .anyRequest()
                        .authenticated() // ...KİMLİK DOĞRULAMASI GEREKTİRSİN
                )

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS (Cross-Origin Resource Sharing) ayarlarını tanımlayan bean.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Ekran görüntünüze göre port 5173
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // !! GÜNCELLEME: Metotları Arrays.asList ile tanımlayalım (daha standart) !!
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // !! GÜNCELLEME: İzin verilen başlıklar için "*" (hepsine izin ver) kullanalım !!
        // Bazen özel başlıklar (custom headers) sorun çıkarabilir.
        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}