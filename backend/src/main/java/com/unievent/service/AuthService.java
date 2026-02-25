package com.unievent.service;

import com.unievent.dto.request.LoginRequest;
import com.unievent.dto.request.RegisterRequest;
import com.unievent.dto.response.AuthResponse;
import com.unievent.entity.Role;
import com.unievent.entity.User;
import com.unievent.repository.UserRepository;
import com.unievent.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kimlik doğrulama servisi — Register ve Login iş mantığı.
 * <p>
 * Neden Controller'da değil de Service'te?
 * → <b>Separation of Concerns</b> (Sorumlulukların Ayrılması) prensibi.
 * Controller sadece HTTP request/response ile ilgilenir;
 * iş mantığı (validation, entity oluşturma, token üretme) Service katmanında
 * kalır.
 * Bu, unit test yazımını kolaylaştırır ve kodun yeniden kullanılabilirliğini
 * artırır.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    /**
     * Yeni kullanıcı kaydı.
     *
     * @throws IllegalArgumentException Kullanıcı adı veya e-posta zaten mevcutsa
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Benzersizlik kontrolü
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Bu kullanıcı adı zaten kullanılıyor: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Bu e-posta adresi zaten kayıtlı: " + request.email());
        }

        // Entity oluştur — Builder pattern ile okunabilirlik artırılır
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .displayName(request.displayName())
                .role(Role.STUDENT) // Varsayılan rol
                .build();

        userRepository.save(user);

        // Kayıt sonrası direkt token üret → kullanıcı tekrar login yapmak zorunda
        // kalmaz
        String token = jwtProvider.generateTokenFromUsername(user.getUsername());
        return AuthResponse.of(token, user.getUsername(), user.getRole().name());
    }

    /**
     * Kullanıcı girişi.
     * <p>
     * {@code authenticationManager.authenticate()} çağrısı:
     * 1. CustomUserDetailsService ile kullanıcıyı DB'den çeker
     * 2. BCrypt ile şifre karşılaştırması yapar
     * 3. Başarısızsa BadCredentialsException fırlatır
     */
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()));

        String token = jwtProvider.generateToken(authentication);

        // Kullanıcının rolünü DB'den çek
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(); // authenticate başarılıysa bu hiç fırlatılmaz

        return AuthResponse.of(token, user.getUsername(), user.getRole().name());
    }
}
