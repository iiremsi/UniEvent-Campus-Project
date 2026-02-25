package com.unievent.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global hata yönetimi — {@code @RestControllerAdvice}.
 * <p>
 * Neden merkezi hata yönetimi?
 * → Her Controller'da ayrı ayrı try-catch yazmak:
 * 1) Kod tekrarına yol açar (DRY ihlali)
 * 2) Farklı hata formatları döner → Frontend tutarsız response parse eder
 * 3) Yeni hata türü eklemek her Controller'ı güncellemeyi gerektirir
 * <p>
 * {@code @RestControllerAdvice} ile:
 * → Tek noktadan tüm hataları yakalayıp <b>tutarlı JSON formatında</b> döneriz.
 * Frontend ekibi her endpoint'ten aynı hata yapısını bekleyebilir.
 *
 * <pre>
 * Hata yanıt formatı (React ekibi için):
 * {
 *   "status": 400,
 *   "error": "Bad Request",
 *   "message": "Bu kullanıcı adı zaten kullanılıyor",
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 * </pre>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Validation hataları — @Valid annotation ile yakalanan field hataları.
     * Her hatalı alan için ayrı mesaj döner.
     *
     * <pre>
     * Örnek yanıt:
     * {
     *   "status": 400,
     *   "error": "Validation Error",
     *   "message": "Girdi doğrulama hatası",
     *   "details": {
     *     "username": "Kullanıcı adı 3-30 karakter arasında olmalıdır",
     *     "email": "Geçerli bir e-posta adresi giriniz"
     *   },
     *   "timestamp": "2024-01-15T10:30:00"
     * }
     * </pre>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Validation Error",
                "message", "Girdi doğrulama hatası",
                "details", fieldErrors,
                "timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * İş mantığı hataları — username/email duplicate vb.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        Map<String, Object> body = Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", "Bad Request",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString());

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Yanlış kullanıcı adı veya şifre.
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(
            BadCredentialsException ex) {

        Map<String, Object> body = Map.of(
                "status", HttpStatus.UNAUTHORIZED.value(),
                "error", "Unauthorized",
                "message", "Kullanıcı adı veya şifre hatalı",
                "timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    /**
     * Kullanıcı bulunamadı.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(
            UsernameNotFoundException ex) {

        Map<String, Object> body = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * Kaynak bulunamadı — genel amaçlı.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex) {

        Map<String, Object> body = Map.of(
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "error", "Internal Server Error",
                "message", "Beklenmeyen bir hata oluştu",
                "timestamp", LocalDateTime.now().toString());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
