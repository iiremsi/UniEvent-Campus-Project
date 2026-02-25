package com.unievent.dto.response;

/**
 * JWT token yanıtı — başarılı login/register sonrası döner.
 *
 * <pre>
 * Örnek JSON yanıt (React ekibi için):
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9...",
 *   "type": "Bearer",
 *   "username": "burak_dev",
 *   "role": "STUDENT"
 * }
 *
 * React ekibi bu token'ı şu şekilde kullanacak:
 * Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
 * </pre>
 */
public record AuthResponse(
        String token,
        String type,
        String username,
        String role) {
    /**
     * Kolaylık factory metodu — type her zaman "Bearer".
     */
    public static AuthResponse of(String token, String username, String role) {
        return new AuthResponse(token, "Bearer", username, role);
    }
}
