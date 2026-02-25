package com.unievent.repository;

import com.unievent.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Beğeni repository'si.
 * <p>
 * Like/Unlike toggle mekanizması:
 * 1. {@code existsByUserIdAndPostId()} → Kullanıcı bu postu daha önce beğenmiş
 * mi?
 * 2. Evet → {@code deleteByUserIdAndPostId()} ile unlike yap
 * 3. Hayır → yeni Like entity'si kaydet
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /** Kullanıcının belirli bir postu beğenip beğenmediğini kontrol eder. */
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /** Unlike işlemi için kullanıcı + post kombinasyonuyla beğeniyi bulur. */
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    /**
     * Belirli bir postun toplam beğeni sayısı (gerektiğinde denormalize sayacı
     * doğrulamak için).
     */
    long countByPostId(Long postId);
}
