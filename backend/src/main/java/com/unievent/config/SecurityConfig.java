package com.unievent.config;

import com.unievent.security.JwtAuthEntryPoint;
import com.unievent.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Spring Security yapılandırması.
 * <p>
 * Kritik mimari kararlar:
 * <ul>
 * <li>{@code SessionCreationPolicy.STATELESS} → Sunucu tarafında session
 * tutulmaz.
 * Her istek JWT token ile doğrulanır. K8s'te pod restart/scale-out durumlarında
 * oturum kaybı yaşanmaz.</li>
 * <li>{@code CSRF disable} → Stateless API'lerde CSRF koruması gereksizdir
 * çünkü
 * session cookie kullanılmıyor; token Authorization header'da taşınıyor.</li>
 * <li>{@code CORS} → React frontend farklı port'ta çalışacak (3000).
 * Tarayıcının
 * cross-origin isteklerine izin vermemiz gerekiyor.</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize kullanımını aktifleştirir
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF kapalı — stateless JWT API
                .csrf(AbstractHttpConfigurer::disable)

                // CORS — React frontend izni
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Exception handling — 401 JSON yanıtı
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthEntryPoint))

                // Session yok — STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // URL bazlı yetkilendirme kuralları
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoint'leri herkese açık
                        .requestMatchers("/api/auth/**").permitAll()
                        // Swagger UI herkese açık
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
                        // GET istekleri → feed görüntüleme (anonim okuma izni)
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        // Diğer tüm istekler authenticated olmalı
                        .anyRequest().authenticated())

                // JWT filter'ı, Spring Security'nin kendi filter'ından ÖNCE çalıştır
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Şifre encoder — BCrypt.
     * BCrypt neden tercih ediliyor?
     * → Salt otomatik eklenir, "cost factor" ile brute-force zorlaştırılır.
     * Her hash farklıdır (aynı şifre bile farklı hash üretir).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager — login işleminde kullanılır.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * CORS ayarları — React frontend (localhost:3000) için.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
