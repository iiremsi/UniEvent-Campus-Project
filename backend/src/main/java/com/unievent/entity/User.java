package com.unievent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Kullanıcı entity'si.
 * <p>
 * Neden {@code @Table(name = "users")}?
 * → "user" bazı veritabanlarında reserved keyword'dür; bu yüzden tablo adı
 * çoğul olarak belirlendi.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Kullanıcı adı boş olamaz")
    @Size(min = 3, max = 30, message = "Kullanıcı adı 3-30 karakter arasında olmalıdır")
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @NotBlank(message = "E-posta boş olamaz")
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "Şifre boş olamaz")
    @Column(nullable = false)
    private String passwordHash;

    @Size(max = 50, message = "Görünen ad en fazla 50 karakter olabilir")
    @Column(length = 50)
    private String displayName;

    @Size(max = 160, message = "Biyografi en fazla 160 karakter olabilir")
    @Column(length = 160)
    private String bio;

    @Column(length = 500)
    private String profileImageUrl;

    /**
     * Kullanıcı rolü: STUDENT, CLUB veya ADMIN.
     * {@code @Enumerated(EnumType.STRING)} ile DB'de "STUDENT" gibi okunabilir
     * string saklanır;
     * ordinal (0,1,2) kullanmak enum sırasına bağımlılık yaratır — best practice
     * değildir.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role = Role.STUDENT;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ── İlişkiler (Lazy — performans için) ──────────────────────

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EventPost> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    // ── Lifecycle Callbacks ─────────────────────────────────────

    /**
     * {@code @PrePersist} — Entity ilk kez DB'ye yazılmadan önce çalışır.
     * Böylece createdAt/updatedAt alanları uygulama seviyesinde otomatik set
     * edilir.
     */
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
