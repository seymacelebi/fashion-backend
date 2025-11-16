package com.fashion.fashion_backend.config;

// Gerekli tüm importlar
import com.fashion.fashion_backend.config.JwtService;
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
import org.springframework.stereotype.Component;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter; // !! HATA BURADAYDI, BU SATIR EKLENDİ !!

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Eğer istek '/api/v1/auth' (veya /api/auth) gibi herkese açık
        // bir yola geliyorsa, bu bir login/register isteğidir, token kontrolü YAPMA.
        final String servletPath = request.getServletPath();
        if (servletPath.contains("/api/v1/auth") ||
                servletPath.contains("/api/auth") ||
                servletPath.contains("/swagger-ui") || // Swagger yollarını da es geç
                servletPath.contains("/v3/api-docs")) { // Swagger yollarını da es geç

            filterChain.doFilter(request, response); // İsteği olduğu gibi bir sonrakine ilet
            return; // Bu filtredeki işlemi bitir
        }

        // --- Buradan sonrası SADECE korumalı yollar için çalışacak ---

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7); // "Bearer " kısmını atla (7 karakter)

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