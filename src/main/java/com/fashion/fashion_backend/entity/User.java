package com.fashion.fashion_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder // Nesne oluşturmayı kolaylaştıran Builder pattern'ı için.
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    // YENİ: Email alanı eklendi. Boş olamaz ve benzersiz olmalı.
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    
 // BİR Kullanıcının, ÇOK Giysisi olabilir.
    // 'mappedBy = "user"' -> Product sınıfındaki 'user' alanının bu ilişkinin sahibi olduğunu belirtir.
    @OneToMany(mappedBy = "user")
    private Set<Product> products;
    
    // BİR Kullanıcının, ÇOK Kombini olabilir.
    @OneToMany(mappedBy = "user")
    private Set<Combination> combinations;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<Role> roles;

    // --- UserDetails METOTLARI ---
    // Rollerimizi Spring Security'nin anladığı formata çeviriyoruz.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }
    // `UserDetails` password alanını implemente ediyoruz.
    @Override
    public String getPassword() {
        return password;
    }

    // Giriş için email kullanacağımız için `getUsername()` metodu email'i dönecek.
    // Spring Security kimlik doğrulamasında bu metodu kullanır.
    @Override
    public String getUsername() {
        return email;
    }
    
    // Uygulamanızda hesap kilitleme, süresinin dolması gibi özellikler yoksa
    // bu metotların true dönmesi yeterlidir.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}