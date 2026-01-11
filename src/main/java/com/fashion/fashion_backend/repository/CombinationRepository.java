package com.fashion.fashion_backend.repository;

import com.fashion.fashion_backend.entity.Combination;
import com.fashion.fashion_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CombinationRepository extends JpaRepository<Combination, Long> {
    List<Combination> findByUser(User user);
}