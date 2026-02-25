package com.unievent.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Kayıt isteği DTO'su — Java Record.
 * <p>
 * Neden Record kullanıyoruz?
 * → Record'lar immutable (değiştirilemez) ve otomatik olarak equals(),
 * hashCode(),
 * toString() metotlarını üretir. DTO'lar için class yerine Record kullanmak
 * modern Java'nın (16+) best practice'idir.
 * <p>
 * Neden Entity direkt dışarı açılmıyor?
 * → Entity'ler DB yapısını temsil eder; passwordHash gibi hassas alanları
 * içerir.
 * DTO pattern sayesinde sadece ihtiyaç duyulan alanlar dışarı verilir
 * ve iç yapı değişse bile API kontratı bozulmaz.
 *
 * <pre>
 * Örnek JSON payload (React ekibi için):
 * POST /api/auth/register
 * {
 *   "username": "burak_dev",
 *   "email": "burak@university.edu.tr",
 *   "password": "SecurePass123!",
 *   "displayName": "Burak Yılmaz"
 * }
 * </pre>
 */
public record RegisterRequest(

        @NotBlank(message = "Kullanıcı adı boş olamaz") @Size(min = 3, max = 30, message = "Kullanıcı adı 3-30 karakter arasında olmalıdır") String username,

        @NotBlank(message = "E-posta boş olamaz") @Email(message = "Geçerli bir e-posta adresi giriniz") String email,

        @NotBlank(message = "Şifre boş olamaz") @Size(min = 6, max = 100, message = "Şifre en az 6 karakter olmalıdır") String password,

        @Size(max = 50, message = "Görünen ad en fazla 50 karakter olabilir") String displayName) {
}
