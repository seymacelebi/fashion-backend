package com.fashion.fashion_backend.service;

import static com.fashion.fashion_backend.entity.dto.AuthDTOs.AuthResponseDto;
import static com.fashion.fashion_backend.entity.dto.AuthDTOs.LoginRequestDto;
import static com.fashion.fashion_backend.entity.dto.AuthDTOs.RegisterRequestDto;

import com.fashion.fashion_backend.config.JwtService;
import com.fashion.fashion_backend.entity.Role;
import com.fashion.fashion_backend.entity.User;
import com.fashion.fashion_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponseDto register(RegisterRequestDto request) {
        // İş Kuralı 1: Email zaten var mı?
        userRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new IllegalArgumentException("Email already in use.");
        });
        // İş Kuralı 2: Kullanıcı adı zaten var mı?
        userRepository.findByUsername(request.username()).ifPresent(u -> {
            throw new IllegalArgumentException("Username already in use.");
        });

        // Yeni kullanıcıyı oluştur
        User newUser = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // ŞİFREYİ HASH'LEME
                .roles(Set.of(Role.ROLE_USER)) // Varsayılan rol ataması
                .build();

        // Kullanıcıyı veritabanına kaydet
        User savedUser = userRepository.save(newUser);

        // JWT Token oluştur
        // UserDetails'in getUsername() metodu email'i döndürdüğü için token email ile üretilir
        String jwtToken = jwtService.generateToken(savedUser);

        // Yanıtı DTO olarak dön
        return new AuthResponseDto(
                jwtToken,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUsername() // Gerçek username alanını döndürüyoruz
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDto login(LoginRequestDto request) {
        // 1. Spring Security'nin kimlik doğrulama yapmasını sağla
        // Bu, UserDetailsService'i (CustomUserDetailsService) tetikler
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),    // UserDetails'teki getUsername() email döndüğü için
                        request.password()
                )
        );

        // 2. Kimlik doğrulama başarılıysa, kullanıcıyı veritabanından bul
        // (UserDetails'imiz bizim User entity'miz olduğu için cast edebiliriz)
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));

        // 3. JWT Token oluştur
        String jwtToken = jwtService.generateToken(user);

        // 4. Yanıtı DTO olarak dön
        return new AuthResponseDto(
                jwtToken,
                user.getId(),
                user.getEmail(),
                user.getUsername()
        );
    }
}
