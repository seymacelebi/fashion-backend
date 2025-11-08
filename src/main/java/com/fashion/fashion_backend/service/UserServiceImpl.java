package com.fashion.fashion_backend.service;

import com.fashion.fashion_backend.entity.User;
import com.fashion.fashion_backend.entity.dto.UserDTOs.UserProfileDto;
import com.fashion.fashion_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize; // Güvenlik için
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service // Bu sınıfın bir Spring Servisi olduğunu belirtir
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * O anda giriş yapmış kullanıcının profilini getirir.
     */
    @Override
    @Transactional(readOnly = true)
    // @PreAuthorize("isAuthenticated()") // Bu metodun çağrılabilmesi için
    // kullanıcının giriş yapmış olması gerekir. (Bu kural genellikle SecurityConfig'de
    // genel olarak belirlenir, ancak burada da belirtilebilir.)
    public UserProfileDto getCurrentUserProfile() {
        // O anda giriş yapmış kullanıcının kimliğini Spring Security'den al
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String userEmail;
        if (principal instanceof UserDetails) {
            userEmail = ((UserDetails) principal).getUsername();
        } else {
            userEmail = principal.toString();
        }

        // Email (bizim 'username'imiz) kullanarak kullanıcıyı veritabanından bul
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Giriş yapmış kullanıcı veritabanında bulunamadı."));

        // Entity'yi DTO'ya çevir ve döndür
        return mapToUserProfileDto(user);
    }

    /**
     * [ADMIN ÖZEL] Tüm kullanıcıları listeler.
     */
    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')") // !! ÖNEMLİ !! Sadece ADMIN rolüne sahip olanlar bu metodu çağırabilir.
    public List<UserProfileDto> getAllUsers() {
        // Veritabanındaki tüm kullanıcıları çek
        List<User> users = userRepository.findAll();

        // Tüm listeyi DTO listesine çevir ve döndür
        return users.stream()
                .map(this::mapToUserProfileDto)
                .collect(Collectors.toList());
    }


    // --- YARDIMCI METOT ---

    /**
     * User (Entity) nesnesini, dış dünyaya güvenle döndürülecek
     * UserProfileDto nesnesine dönüştürür.
     */
    private UserProfileDto mapToUserProfileDto(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
