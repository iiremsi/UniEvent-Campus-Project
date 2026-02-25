# UniEvent — Veritabanı ER Diyagramı

## İlişkisel Yapı

```mermaid
erDiagram
    USERS ||--o{ EVENT_POSTS : creates
    USERS ||--o{ LIKES : gives
    USERS ||--o{ COMMENTS : writes
    EVENT_POSTS ||--o{ LIKES : receives
    EVENT_POSTS ||--o{ COMMENTS : has

    USERS {
        bigint id PK
        varchar username UK "unique, 3-30 char"
        varchar email UK "unique"
        varchar password_hash "BCrypt encoded"
        varchar display_name "max 50 char"
        varchar bio "max 160 char"
        varchar profile_image_url
        enum role "STUDENT | CLUB | ADMIN"
        timestamp created_at
        timestamp updated_at
    }

    EVENT_POSTS {
        bigint id PK
        bigint author_id FK "→ users.id"
        varchar content "max 280 char (tweet benzeri)"
        varchar event_title "max 100 char"
        varchar event_location "max 150 char"
        timestamp event_date
        varchar image_url
        int like_count "denormalized"
        int comment_count "denormalized"
        timestamp created_at
        timestamp updated_at
    }

    LIKES {
        bigint id PK
        bigint user_id FK "→ users.id"
        bigint post_id FK "→ event_posts.id"
        timestamp created_at
        "UNIQUE(user_id, post_id)"
    }

    COMMENTS {
        bigint id PK
        bigint user_id FK "→ users.id"
        bigint post_id FK "→ event_posts.id"
        varchar content "max 500 char"
        timestamp created_at
        timestamp updated_at
    }
```

## Tasarım Kararları

| Karar | Neden |
|---|---|
| `like_count` / `comment_count` denormalized | Feed sorgusunda her post için COUNT subquery yerine O(1) okuma |
| `UNIQUE(user_id, post_id)` on LIKES | Bir kullanıcının aynı postu birden fazla beğenmesini DB seviyesinde engeller |
| `role` enum string (`@Enumerated(STRING)`) | Ordinal (0,1,2) sıra bağımlılığı yaratır — string daha güvenli |
| `@PrePersist` / `@PreUpdate` | Timestamp'ler uygulama seviyesinde otomatik yönetilir |
| `FetchType.LAZY` tüm ilişkilerde | İlişkili entity'ler sadece erişildiğinde yüklenir — performans |
