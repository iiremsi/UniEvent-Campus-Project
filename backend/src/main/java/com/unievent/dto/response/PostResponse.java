package com.unievent.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Gönderi yanıt DTO'su — Feed ve tekil post görüntüleme için kullanılır.
 * <p>
 * Neden Entity direkt dönmüyoruz?
 * → 1) Entity'de passwordHash gibi hassas ilişkili alanlar var
 * 2) Entity'de lazy-loaded collection'lar var → serialize ederken
 * LazyInitializationException patlar
 * 3) API kontratı (bu Record) ile DB yapısı (Entity) birbirinden bağımsız kalır
 * → DB'de değişiklik yapınca API bozulmaz
 *
 * <pre>
 * Örnek JSON yanıt (React ekibi için):
 * {
 *   "id": 42,
 *   "content": "Yarın 14:00'te Bilgisayar Kulübü toplantısı var! 🎉",
 *   "eventTitle": "Bilgisayar Kulübü Haftalık Toplantı",
 *   "eventLocation": "Mühendislik Fakültesi B-201",
 *   "eventDate": "2024-03-15T14:00:00",
 *   "imageUrl": "https://example.com/event-banner.jpg",
 *   "authorId": 1,
 *   "authorUsername": "bilgisayar_kulubu",
 *   "authorDisplayName": "Bilgisayar Kulübü",
 *   "createdAt": "2024-03-14T09:30:00"
 * }
 * </pre>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostResponse(
        Long id,
        String content,
        String eventTitle,
        String eventLocation,
        LocalDateTime eventDate,
        String imageUrl,
        Long authorId,
        String authorUsername,
        String authorDisplayName,
        LocalDateTime createdAt) {
}
