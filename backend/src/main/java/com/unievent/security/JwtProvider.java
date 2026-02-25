package com.unievent.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT token üretme ve doğrulama yardımcı sınıfı.
 * <p>
 * Neden JWT (JSON Web Token)?
 * → Uygulamamız <b>stateless</b> olmalı. Kubernetes ortamında pod'lar yeniden
 * başladığında veya scale out yapıldığında, sunucu tarafında session tutmak
 * tutarsızlığa yol açar (sticky session veya session replication gerekir).
 * <p>
 * JWT ile oturum bilgisi token içinde taşınır → herhangi bir pod isteği
 * işleyebilir.
 * Bu, Kubernetes'te <b>horizontal scaling</b> için kritiktir.
 *
 * <pre>
 * Token yapısı:
 * Header:  {"alg": "HS256", "typ": "JWT"}
 * Payload: {"sub": "burak_dev", "iat": 1700000000, "exp": 1700086400}
 * </pre>
 */
@Component
public class JwtProvider {

    private final SecretKey key;
    private final long jwtExpirationMs;

    public JwtProvider(
            @Value("${app.jwt.secret}") String jwtSecret,
            @Value("${app.jwt.expiration-ms}") long jwtExpirationMs) {
        // HMAC-SHA256 anahtarı oluştur — secret en az 256 bit olmalı
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /**
     * Kullanıcı authentication bilgisinden JWT token üretir.
     *
     * @param authentication Spring Security authentication objesi
     * @return Signed JWT string
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateTokenFromUsername(userDetails.getUsername());
    }

    /**
     * Kullanıcı adından direkt token üretir (register sonrası kullanılır).
     */
    public String generateTokenFromUsername(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * Token'dan kullanıcı adını çıkarır.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Token'ın geçerliliğini doğrular.
     * Süresi dolmuş, imzası hatalı veya boş token'lar reddedilir.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // ExpiredJwtException, MalformedJwtException, SignatureException vb.
            return false;
        }
    }
}
