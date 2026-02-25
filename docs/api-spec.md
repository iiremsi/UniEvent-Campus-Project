# UniEvent API Referansı

## Base URL
```
http://localhost:8080/api
```

## Swagger UI
```
http://localhost:8080/swagger-ui.html
```

---

## Authentication

### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "burak_dev",
  "email": "burak@university.edu.tr",
  "password": "SecurePass123!",
  "displayName": "Burak Yılmaz"
}
```

**Response (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "burak_dev",
  "role": "STUDENT"
}
```

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "burak_dev",
  "password": "SecurePass123!"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "burak_dev",
  "role": "STUDENT"
}
```

---

## Posts

> Token gerektiren endpoint'lerde header'a şunu ekleyin:
> `Authorization: Bearer <token>`

### Gönderi Oluştur (Auth Required)
```http
POST /api/posts
Authorization: Bearer eyJhbG...
Content-Type: application/json

{
  "content": "Yarın 14:00'te Bilgisayar Kulübü toplantısı var! 🎉",
  "eventTitle": "Bilgisayar Kulübü Haftalık Toplantı",
  "eventLocation": "Mühendislik Fakültesi B-201",
  "eventDate": "2024-03-15T14:00:00",
  "imageUrl": null
}
```

**Response (201 Created):**
```json
{
  "id": 42,
  "content": "Yarın 14:00'te Bilgisayar Kulübü toplantısı var! 🎉",
  "eventTitle": "Bilgisayar Kulübü Haftalık Toplantı",
  "eventLocation": "Mühendislik Fakültesi B-201",
  "eventDate": "2024-03-15T14:00:00",
  "authorId": 1,
  "authorUsername": "burak_dev",
  "authorDisplayName": "Burak Yılmaz",
  "createdAt": "2024-03-14T09:30:00"
}
```

### Feed (Public)
```http
GET /api/posts?page=0&size=20
```

**Response (200 OK):**
```json
{
  "content": [ ...PostResponse array... ],
  "totalElements": 150,
  "totalPages": 8,
  "number": 0,
  "size": 20,
  "first": true,
  "last": false
}
```

### Tekil Gönderi (Public)
```http
GET /api/posts/{id}
```

### Kullanıcı Gönderileri (Public)
```http
GET /api/posts/user/{userId}?page=0&size=20
```

### Gönderi Sil (Auth Required — Sadece Yazar)
```http
DELETE /api/posts/{id}
Authorization: Bearer eyJhbG...
```
**Response: 204 No Content**

---

## Hata Yanıt Formatı

Tüm hata yanıtları aynı yapıdadır:

```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Girdi doğrulama hatası",
  "details": {
    "username": "Kullanıcı adı 3-30 karakter arasında olmalıdır"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

| Status | Açıklama |
|---|---|
| 400 | Validation hatası veya iş mantığı hatası |
| 401 | Geçersiz/eksik JWT token |
| 404 | Kaynak bulunamadı |
| 500 | Sunucu hatası |
