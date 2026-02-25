package com.unievent.controller;

import com.unievent.dto.request.CreatePostRequest;
import com.unievent.dto.response.PostResponse;
import com.unievent.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Gönderi (Post) Controller'ı — Feed ve CRUD endpoint'leri.
 * <p>
 * {@code @AuthenticationPrincipal} neden kullanıyoruz?
 * → SecurityContext'ten authenticated kullanıcıyı otomatik inject eder.
 * Manuel olarak {@code SecurityContextHolder.getContext().getAuthentication()}
 * yazmaya
 * gerek kalmaz — daha temiz ve test edilebilir kod.
 * <p>
 * {@code @PageableDefault} neden?
 * → Frontend sayfalama parametresi göndermezse varsayılan değerler uygulanır.
 * Böylece API hiçbir zaman tüm veritabanını tek seferde dökmez.
 *
 * <pre>
 * React ekibi için API kullanım örnekleri:
 *
 * Feed çekme (sayfa 0, 20 post):
 *   GET /api/posts?page=0&size=20
 *
 * Feed çekme (sayfa 2, 10 post):
 *   GET /api/posts?page=2&size=10
 *
 * Kullanıcı profili gönderileri:
 *   GET /api/posts/user/5?page=0&size=20
 * </pre>
 */
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Etkinlik gönderileri ve feed")
public class PostController {

    private final PostService postService;

    /**
     * Yeni gönderi oluşturur.
     *
     * <pre>
     * POST /api/posts
     * Headers: Authorization: Bearer eyJhbG...
     * Body:
     * {
     *   "content": "Yarın 14:00'te Bilgisayar Kulübü toplantısı var! 🎉",
     *   "eventTitle": "Bilgisayar Kulübü Haftalık Toplantı",
     *   "eventLocation": "Mühendislik Fakültesi B-201",
     *   "eventDate": "2024-03-15T14:00:00",
     *   "imageUrl": null
     * }
     *
     * Response (201 Created):
     * {
     *   "id": 42,
     *   "content": "Yarın 14:00'te ...",
     *   "eventTitle": "Bilgisayar Kulübü ...",
     *   "authorId": 1,
     *   "authorUsername": "burak_dev",
     *   "authorDisplayName": "Burak Yılmaz",
     *   "createdAt": "2024-03-14T09:30:00"
     * }
     * </pre>
     */
    @PostMapping
    @Operation(summary = "Yeni gönderi oluştur", description = "Authenticated kullanıcı adına etkinlik gönderisi oluşturur")
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody CreatePostRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        PostResponse response = postService.createPost(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Ana Feed — Tüm gönderiler, sayfalanmış.
     * <p>
     * {@code @PageableDefault(size = 20, sort = "createdAt", direction = DESC)}
     * → Frontend parametre göndermezse: ilk 20 gönderi, en yeniden en eskiye.
     *
     * <pre>
     * GET /api/posts?page=0&size=20
     *
     * Response (200 OK):
     * {
     *   "content": [ ... PostResponse array ... ],
     *   "totalElements": 150,
     *   "totalPages": 8,
     *   "number": 0,
     *   "size": 20,
     *   "first": true,
     *   "last": false
     * }
     * </pre>
     */
    @GetMapping
    @Operation(summary = "Feed — Tüm gönderileri listele", description = "Sayfalanmış gönderi akışı. Varsayılan: 20 gönderi/sayfa")
    public ResponseEntity<Page<PostResponse>> getFeed(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(postService.getFeed(pageable));
    }

    /**
     * Tekil gönderi görüntüleme.
     *
     * <pre>
     * GET / api / posts / 42
     * </pre>
     */
    @GetMapping("/{id}")
    @Operation(summary = "Gönderi detayı", description = "ID ile tekil gönderi getirir")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * Belirli bir kullanıcının gönderileri — profil sayfası.
     *
     * <pre>
     * GET /api/posts/user/5?page=0&size=20
     * </pre>
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Kullanıcı gönderileri", description = "Belirli bir kullanıcının gönderilerini sayfalanmış listeler")
    public ResponseEntity<Page<PostResponse>> getPostsByUser(
            @PathVariable Long userId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(postService.getPostsByUser(userId, pageable));
    }

    /**
     * Gönderi silme — sadece yazar silebilir.
     *
     * <pre>
     * DELETE /api/posts/42
     * Headers: Authorization: Bearer eyJhbG...
     *
     * Response: 204 No Content
     * </pre>
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Gönderi sil", description = "Sadece yazarı tarafından silinebilir")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        postService.deletePost(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
