package com.fashion.fashion_backend.repository;

import com.fashion.fashion_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Email ile kullanıcı bulmak için yeni metot.
    Optional<User> findByEmail(String email);

    // Kullanıcı adının benzersizliğini kontrol etmek için bu metot kalmalı.
    Optional<User> findByUsername(String username);
}