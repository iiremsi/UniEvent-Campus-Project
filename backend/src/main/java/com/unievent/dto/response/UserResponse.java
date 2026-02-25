package com.unievent.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Kullanıcı profili yanıtı.
 * <p>
 * {@code @JsonInclude(NON_NULL)} — null olan alanlar JSON'a dahil edilmez.
 * Böylece bio veya profileImageUrl set edilmemişse yanıt temiz kalır.
 *
 * <pre>
 * Örnek JSON yanıt (React ekibi için):
 * {
 *   "id": 1,
 *   "username": "burak_dev",
 *   "email": "burak@university.edu.tr",
 *   "displayName": "Burak Yılmaz",
 *   "bio": "Backend Developer",
 *   "profileImageUrl": "https://...",
 *   "role": "STUDENT",
 *   "createdAt": "2024-01-15T10:30:00"
 * }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        Long id,
        String username,
        String email,
        String displayName,
        String bio,
        String profileImageUrl,
        String role,
        LocalDateTime createdAt) {
}
