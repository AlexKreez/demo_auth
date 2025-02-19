package com.example.demo.services;

import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//  ищет пользователя по логину, получает его пароль и роли, а затем передает дальше

        System.out.println("🔍 Загружаем пользователя: " + username);

        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("❌ Пользователь не найден: " + username));

        System.out.println("✅ Найден пользователь: " + user.getLogin() + " (ID: " + user.getId() + ")");

        // Загружаем роли пользователя из БД
        List<UserRole> roles = userRoleRepository.findByUserId(user.getId());
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());

        System.out.println("✅ Роли пользователя " + username + ": " + authorities);

        System.out.println("🔍 Итоговые роли для " + user.getLogin() + ": " + authorities);

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                authorities
        );
    }
}
//Добавить кеширование пользователей
//Сейчас каждый раз при логине мы делаем SQL-запрос
//Можно кешировать пользователей, чтобы не делать повторные запросы