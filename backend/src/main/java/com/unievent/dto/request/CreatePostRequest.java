package com.unievent.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Gönderi oluşturma isteği.
 *
 * <pre>
 * Örnek JSON payload (React ekibi için):
 * POST /api/posts
 * Headers: Authorization: Bearer eyJhbG...
 * {
 *   "content": "Yarın 14:00'te Bilgisayar Kulübü toplantısı var! 🎉",
 *   "eventTitle": "Bilgisayar Kulübü Haftalık Toplantı",
 *   "eventLocation": "Mühendislik Fakültesi B-201",
 *   "eventDate": "2024-03-15T14:00:00",
 *   "imageUrl": "https://example.com/event-banner.jpg"
 * }
 * </pre>
 */
public record CreatePostRequest(

        @NotBlank(message = "Gönderi içeriği boş olamaz") @Size(max = 280, message = "Gönderi en fazla 280 karakter olabilir") String content,

        @Size(max = 100, message = "Etkinlik başlığı en fazla 100 karakter olabilir") String eventTitle,

        @Size(max = 150, message = "Konum en fazla 150 karakter olabilir") String eventLocation,

        LocalDateTime eventDate,

        String imageUrl) {
}
