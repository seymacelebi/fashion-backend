package com.fashion.fashion_backend.config;

import com.fashion.fashion_backend.config.JwtService; // 'security' paketinden import
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component; // <-- BU ANOTASYON KRİTİK!
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Spring'in bu sınıfı yönetmesini ve içine 'bean'leri enjekte etmesini sağlar
@RequiredArgsConstructor // 'final' alanlar için constructor'ı otomatik oluşturur
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // @RequiredArgsConstructor sayesinde Spring bu alanları OTOMATİK OLARAK doldurur.
    // 'this.jwtService' ASLA 'null' OLMAZ.
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // "Bearer " kısmını atla (7 karakter)
        
        // Hatanın olduğu yer burasıydı. 'jwtService' artık null olmayacak.
        userEmail = jwtService.extractUsername(jwt); 

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}