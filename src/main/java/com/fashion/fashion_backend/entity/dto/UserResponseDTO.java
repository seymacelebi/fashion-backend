package com.fashion.fashion_backend.entity.dto;

import com.fashion.fashion_backend.entity.Role;
import com.fashion.fashion_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles; // Roller artık String olarak tutuluyor

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(), // Bu hala username olarak kalabilir.
                user.getEmail(),
                // DÜZELTME: Set<Role> -> Set<String> dönüşümü yapıldı.
                user.getRoles().stream().map(Role::name).collect(Collectors.toSet())
        );
    }
}