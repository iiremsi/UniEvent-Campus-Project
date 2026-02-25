package com.unievent.repository;

import com.unievent.entity.EventPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Etkinlik gönderi repository'si.
 * <p>
 * Neden {@link Pageable} kullanıyoruz?
 * → Feed sayfasında binlerce gönderi olabilir. Tümünü tek seferde çekmek:
 * 1) Belleği tüketir (OutOfMemoryError riski)
 * 2) Network payload'ını şişirir
 * 3) Frontend'de render performansını düşürür
 * <p>
 * {@code Pageable} ile sayfa numarası ve sayfa boyutu (örn. ?page=0&size=20)
 * parametreleri
 * alınır ve SQL'e {@code LIMIT/OFFSET} olarak yansıtılır. Böylece her istekte
 * sadece
 * ihtiyaç duyulan kadar veri transfer edilir.
 * <p>
 * Dönen {@link Page} objesi şu meta verileri içerir:
 * - totalElements (toplam kayıt sayısı)
 * - totalPages (toplam sayfa sayısı)
 * - number (mevcut sayfa)
 * - hasNext / hasPrevious
 */
@Repository
public interface EventPostRepository extends JpaRepository<EventPost, Long> {

    /**
     * Ana Feed — Tüm gönderiler, en yeniden en eskiye.
     * SQL: SELECT * FROM event_posts ORDER BY created_at DESC LIMIT ? OFFSET ?
     */
    Page<EventPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Kullanıcı Profili — Belirli bir kullanıcının gönderileri.
     * SQL: SELECT * FROM event_posts WHERE author_id = ? ORDER BY created_at DESC
     * LIMIT ? OFFSET ?
     */
    Page<EventPost> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);
}
