package com.unievent.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Login isteği DTO'su.
 *
 * <pre>
 * Örnek JSON payload (React ekibi için):
 * POST /api/auth/login
 * {
 *   "username": "burak_dev",
 *   "password": "SecurePass123!"
 * }
 * </pre>
 */
public record LoginRequest(

        @NotBlank(message = "Kullanıcı adı boş olamaz") String username,

        @NotBlank(message = "Şifre boş olamaz") String password) {
}
