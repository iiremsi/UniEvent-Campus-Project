package com.unievent.security;

import com.unievent.entity.User;
import com.unievent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Spring Security'nin kullanıcı bilgilerini yüklemek için kullandığı servis.
 * <p>
 * Spring Security kendi {@link UserDetails} arayüzünü kullanır.
 * Bizim {@link User} entity'mizi bu arayüze dönüştürmemiz gerekir.
 * <p>
 * {@code ROLE_} prefix'i Spring Security konvansiyonudur:
 * → hasRole("STUDENT") kontrolü aslında "ROLE_STUDENT" authority'sini arar.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Kullanıcı bulunamadı: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }
}
