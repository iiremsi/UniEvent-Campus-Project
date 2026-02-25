package com.unievent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Etkinlik gönderi entity'si (Tweet benzeri).
 * <p>
 * {@code likeCount} ve {@code commentCount} neden denormalize?
 * → Feed sayfasında N adet post gösterirken her biri için COUNT(*) sorgusu
 * atmak
 * O(N) ek sorgu demektir. Denormalize sayaçlar sayesinde tek bir SELECT ile
 * tüm veriler gelir. Like/Unlike ve Comment ekleme/silme işlemlerinde
 * {@code @Transactional} içinde bu sayaçlar güncellenir.
 */
@Entity
@Table(name = "event_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Gönderiyi oluşturan kullanıcı.
     * {@code FetchType.LAZY} — Post yüklendiğinde User otomatik çekilmez,
     * sadece erişildiğinde sorgu atılır. Performans optimizasyonu.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @NotBlank(message = "Gönderi içeriği boş olamaz")
    @Size(max = 280, message = "Gönderi en fazla 280 karakter olabilir")
    @Column(nullable = false, length = 280)
    private String content;

    @Size(max = 100, message = "Etkinlik başlığı en fazla 100 karakter olabilir")
    @Column(length = 100)
    private String eventTitle;

    @Size(max = 150, message = "Konum en fazla 150 karakter olabilir")
    @Column(length = 150)
    private String eventLocation;

    private LocalDateTime eventDate;

    @Column(length = 500)
    private String imageUrl;

    /**
     * Denormalized beğeni sayacı.
     * Varsayılan 0 — yeni post oluşturulduğunda sıfır beğeni ile başlar.
     */
    @Column(nullable = false)
    @Builder.Default
    private int likeCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private int commentCount = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ── İlişkiler ───────────────────────────────────────────────

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // ── Lifecycle Callbacks ─────────────────────────────────────

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
