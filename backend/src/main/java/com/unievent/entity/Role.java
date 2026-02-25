package com.unievent.entity;

/**
 * Kullanıcı rolleri.
 * <p>
 * STUDENT — Etkinlikleri görüntüleyip etkileşime geçebilir.
 * CLUB    — Kulüp hesabı; etkinlik oluşturabilir.
 * ADMIN   — Tüm yetkilere sahiptir, içerik moderasyonu yapabilir.
 */
public enum Role {
    STUDENT,
    CLUB,
    ADMIN
}
