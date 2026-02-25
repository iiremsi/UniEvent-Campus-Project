package com.unievent.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Beğeni entity'si.
 * <p>
 * {@code @UniqueConstraint({"user_id", "post_id"})} →
 * Bir kullanıcı aynı gönderiyi yalnızca bir kez beğenebilir.
 * Bu kısıtlama DB seviyesinde zorunlu kılınır (uygulama katmanında da kontrol
 * edilir).
 * <p>
 * Neden ayrı bir Entity ve tablo?
 * → Many-to-Many ilişkide ek alan (createdAt) tutabilmek için
 * join table yerine ayrı entity tercih edildi. Bu pattern "Association Entity"
 * olarak bilinir.
 */
@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(name = "uk_likes_user_post", columnNames = { "user_id",
        "post_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private EventPost post;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
