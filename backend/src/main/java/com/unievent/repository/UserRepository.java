package com.unievent.repository;

import com.unievent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Kullanıcı repository'si.
 * <p>
 * Spring Data JPA, bu interface'i otomatik olarak implement eder.
 * Metot isimlendirme kurallarına (Query Derivation) göre SQL sorguları otomatik
 * üretilir.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /** Login işlemi için kullanıcı adıyla arama. */
    Optional<User> findByUsername(String username);

    /** E-posta ile kullanıcı arama (şifre sıfırlama vb. için). */
    Optional<User> findByEmail(String email);

    /** Kayıt sırasında kullanıcı adı benzersizlik kontrolü. */
    boolean existsByUsername(String username);

    /** Kayıt sırasında e-posta benzersizlik kontrolü. */
    boolean existsByEmail(String email);
}
