package com.unievent.repository;

import com.unievent.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Yorum repository'si.
 * <p>
 * Yorumlar da sayfalanır çünkü popüler bir gönderinin yüzlerce yorumu olabilir.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Belirli bir gönderinin yorumları, en yeniden en eskiye.
     * Paginated — Frontend sonsuz kaydırma (infinite scroll) yapabilir.
     */
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
}
