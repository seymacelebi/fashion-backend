package com.fashion.fashion_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger (OpenAPI) yapılandırma sınıfı.
 * Bu sınıf, Swagger UI'a bir "Authorize" butonu ekler
 * ve JWT (Bearer Token) kullanarak API'larımızı nasıl test edeceğimizi tanımlar.
 */
@Configuration
// 1. API'mız için genel bir tanım (Başlık, Versiyon)
@OpenAPIDefinition(
    info = @Info(title = "StilRehberi API", version = "v1", description = "StilRehberi projesi için backend API'leri"),
    // 2. TÜM endpoint'lerin "bearerAuth" (JWT) gerektirdiğini global olarak belirt
    security = @SecurityRequirement(name = "bearerAuth")
)
// 3. "bearerAuth" adında bir güvenlik şeması tanımla
@SecurityScheme(
    name = "bearerAuth", // Yukarıdaki @SecurityRequirement'daki 'name' ile aynı olmalı
    type = SecuritySchemeType.HTTP, // Güvenlik tipi HTTP
    scheme = "bearer", // Kullanılan şema "Bearer"
    bearerFormat = "JWT" // Formatı JWT
)
public class OpenApiConfig {
    // Bu sınıfın içi boş olabilir. 
    // Tüm yapılandırma yukarıdaki anotasyonlar ile yapılır.
}
