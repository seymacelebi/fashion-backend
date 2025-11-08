package com.fashion.fashion_backend.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.Set;

/**
 * Kullanıcı rollerini güncelleme isteğinin gövdesini (request body) temsil eden DTO.
 */
@Data // lombok: Getter, Setter, toString, equals, hashCode metotlarını otomatik oluşturur.
public class UpdateRolesRequest {

    /**
     * Kullanıcıya atanacak yeni rollerin listesi.
     * Roller String olarak alınır (örn: "ROLE_USER", "ROLE_ADMIN").
     * @NotEmpty anotasyonu, bu listenin boş veya null olmamasını sağlar.
     */
    @NotEmpty(message = "Rol listesi boş olamaz.")
    private Set<String> roles;
}