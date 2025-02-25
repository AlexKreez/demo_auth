package com.example.demo.services;

import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);


    @Override
    @Cacheable(value = "users", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//  ищет пользователя по логину, получает его пароль и роли, а затем передает дальше

        LOGGER.info("🔍 Загружаем пользователя: {}", username);

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("❌ Пользователь не найден: " + username));

        LOGGER.info("✅ Найден пользователь: {} (ID: {})", user.getLogin(), user.getId());

        // Загружаем роли пользователя из БД
        List<UserRole> roles = userRoleRepository.findByUserId(user.getId());
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        LOGGER.info("✅ Роли пользователя {}: {}", username, authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                authorities
        );
    }
}