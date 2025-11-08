package com.fashion.fashion_backend.config;

import com.fashion.fashion_backend.entity.Role;
import com.fashion.fashion_backend.entity.User;
import com.fashion.fashion_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner; // Spring'in "başlangıçta çalıştır" arayüzü
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Uygulama ilk çalıştığında veritabanını kontrol eder
 * ve eğer bir ADMIN kullanıcısı yoksa, varsayılan bir ADMIN oluşturur.
 */
@Component // Spring'e "Bu sınıfı yönet" der.
public class DataSeeder implements CommandLineRunner { // "Beni başlangıçta çalıştır" der.

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // application.properties'ten değerleri çekiyoruz
    @Value("${admin.default.username}")
    private String adminUsername;

    @Value("${admin.default.email}")
    private String adminEmail;

    @Value("${admin.default.password}")
    private String adminPassword;

    // "Müdür, bana 'Arşiv Odası' (UserRepository) ve 'Şifreleyici Makine' (PasswordEncoder) lazım"
    @Autowired
    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Bu metot, uygulama tam olarak başladığında Spring tarafından OTOMATİK olarak çağrılır.
     */
    @Override
    public void run(String... args) throws Exception {
        
        // 1. KONTROL: "Arşiv odasına (DB) bak, bu email'de biri var mı?"
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            
            // 2. OLUŞTUR: "Yokmuş. O zaman yeni bir kimlik kartı (User) oluştur."
            User adminUser = User.builder()
                    .username(adminUsername)
                    .email(adminEmail)
                    // 3. HASH'LE: "Şifreyi al, şifreleyici makineden geçir ve GÜVENLİ halini kaydet."
                    .password(passwordEncoder.encode(adminPassword)) 
                    // 4. ROL ATA: "Bu kişiye hem ADMIN hem de USER rollerini ver."
                    .roles(Set.of(Role.ROLE_ADMIN, Role.ROLE_USER)) 
                    .build();
            
            // 5. KAYDET: "Bu yeni, tam yetkili, güvenli kimlik kartını arşive (veritabanına) kaydet."
            userRepository.save(adminUser);
            System.out.println("Varsayılan ADMIN kullanıcısı oluşturuldu: " + adminEmail);
        
        } else {
            // "Arşivde zaten varmış. Hiçbir şey yapma."
            System.out.println("Varsayılan ADMIN kullanıcısı zaten mevcut.");
        }
    }
}


