package com.fashion.fashion_backend.repository;

import com.fashion.fashion_backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    // SQL: SELECT * FROM brands WHERE name = 'gelenIsim'
    // Optional kullanıyoruz çünkü marka veritabanında olmayabilir.
    Optional<Brand> findByName(String name);
}