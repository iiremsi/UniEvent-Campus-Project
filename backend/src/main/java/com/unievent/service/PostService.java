package com.unievent.service;

import com.unievent.dto.request.CreatePostRequest;
import com.unievent.dto.response.PostResponse;
import com.unievent.entity.EventPost;
import com.unievent.entity.User;
import com.unievent.repository.EventPostRepository;
import com.unievent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Gönderi servisi — CRUD ve Feed iş mantığı.
 * <p>
 * {@code @Transactional(readOnly = true)} neden?
 * → Read-only transaction'lar Hibernate'e "dirty checking yapma" sinyali verir.
 * Bu, SELECT sorgularında gereksiz flush çağrısını engeller ve performansı
 * artırır.
 * Yazma işlemleri olan metotlarda {@code @Transactional} (readOnly = false)
 * kullanılır.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final EventPostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Yeni gönderi oluşturur.
     * <p>
     * Authenticated kullanıcının username'i SecurityContext'ten alınır ve
     * Controller tarafından bu metoda iletilir.
     */
    @Transactional
    public PostResponse createPost(CreatePostRequest request, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Kullanıcı bulunamadı: " + username));

        EventPost post = EventPost.builder()
                .author(author)
                .content(request.content())
                .eventTitle(request.eventTitle())
                .eventLocation(request.eventLocation())
                .eventDate(request.eventDate())
                .imageUrl(request.imageUrl())
                .build();

        EventPost saved = postRepository.save(post);
        return mapToResponse(saved);
    }

    /**
     * Ana Feed — Tüm gönderiler, en yeniden en eskiye, sayfalanmış.
     * <p>
     * {@link Pageable} parametresi Controller'dan gelir:
     * → {@code ?page=0&size=20&sort=createdAt,desc}
     * <p>
     * Dönen {@link Page} objesi şu bilgileri taşır:
     * - content: gönderi listesi
     * - totalElements: toplam gönderi sayısı
     * - totalPages: toplam sayfa sayısı
     * - number: mevcut sayfa numarası
     * - hasNext / hasPrevious: sayfalama navigasyonu
     */
    public Page<PostResponse> getFeed(Pageable pageable) {
        return postRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToResponse);
    }

    /**
     * Belirli bir kullanıcının gönderileri — profil sayfası için.
     */
    public Page<PostResponse> getPostsByUser(Long userId, Pageable pageable) {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::mapToResponse);
    }

    /**
     * Tekil gönderi görüntüleme.
     */
    public PostResponse getPostById(Long postId) {
        EventPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Gönderi bulunamadı: " + postId));
        return mapToResponse(post);
    }

    /**
     * Gönderi silme — sadece yazar silebilir.
     */
    @Transactional
    public void deletePost(Long postId, String username) {
        EventPost post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Gönderi bulunamadı: " + postId));

        if (!post.getAuthor().getUsername().equals(username)) {
            throw new IllegalArgumentException(
                    "Bu gönderiyi silme yetkiniz yok");
        }

        postRepository.delete(post);
    }

    // ── Entity → DTO Dönüşümü ──────────────────────────────────

    /**
     * Entity'yi Response DTO'ya dönüştürür.
     * Bu dönüşüm sayesinde Entity'nin iç yapısı (lazy collection'lar, passwordHash
     * vb.)
     * asla dışarıya sızmaz.
     */
    private PostResponse mapToResponse(EventPost post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getEventTitle(),
                post.getEventLocation(),
                post.getEventDate(),
                post.getImageUrl(),
                post.getAuthor().getId(),
                post.getAuthor().getUsername(),
                post.getAuthor().getDisplayName(),
                post.getCreatedAt());
    }
}
