package com.unievent.controller;

import com.unievent.dto.request.LoginRequest;
import com.unievent.dto.request.RegisterRequest;
import com.unievent.dto.response.AuthResponse;
import com.unievent.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kimlik doğrulama controller'ı.
 * <p>
 * Tüm endpoint'ler {@code /api/auth/**} altında ve SecurityConfig'te
 * {@code permitAll()} ile
 * herkese açık tanımlıdır — login/register için token gerekli değildir.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Kayıt ve giriş işlemleri")
public class AuthController {

    private final AuthService authService;

    /**
     * Yeni kullanıcı kaydı.
     *
     * <pre>
     * POST /api/auth/register
     * Request Body:
     * {
     *   "username": "burak_dev",
     *   "email": "burak@university.edu.tr",
     *   "password": "SecurePass123!",
     *   "displayName": "Burak Yılmaz"
     * }
     *
     * Response (201 Created):
     * {
     *   "token": "eyJhbGciOiJIUzI1NiJ9...",
     *   "type": "Bearer",
     *   "username": "burak_dev",
     *   "role": "STUDENT"
     * }
     * </pre>
     */
    @PostMapping("/register")
    @Operation(summary = "Yeni kullanıcı kaydı", description = "Kullanıcı oluşturur ve JWT token döner")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Kullanıcı girişi.
     *
     * <pre>
     * POST /api/auth/login
     * Request Body:
     * {
     *   "username": "burak_dev",
     *   "password": "SecurePass123!"
     * }
     *
     * Response (200 OK):
     * {
     *   "token": "eyJhbGciOiJIUzI1NiJ9...",
     *   "type": "Bearer",
     *   "username": "burak_dev",
     *   "role": "STUDENT"
     * }
     * </pre>
     */
    @PostMapping("/login")
    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile giriş yapar, JWT token döner")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
