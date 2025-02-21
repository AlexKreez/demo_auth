package com.example.demo.services;


import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.domain.entityUser.UserRoleId;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminService.class);


    @Cacheable(value = "userRoles", key = "#login")
    public List<String> getUserRolesByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("❌ Пользователь не найден"));
        LOGGER.info("🔍 Загружаем роли пользователя '{}'", login);
        List<String> roles = userRoleRepository.findByUserId(user.getId())
                .stream().map(UserRole::getRole).collect(Collectors.toList());
        LOGGER.info("✅ Роли пользователя '{}': {}", login, roles);
        return roles;

    }

    @Transactional
    @CacheEvict(value = "userRoles", key = "#userId")
    public void addRoleToUser(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Пользователь не найден"));

        if (userRoleRepository.findById(new UserRoleId(userId, role)).isPresent()) {
            throw new IllegalArgumentException("⚠️ Роль уже назначена пользователю");
        }

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
        LOGGER.info("✅ Роль '{}' добавлена пользователю '{}'", role, userId);
    }

    @Transactional
    @CacheEvict(value = "userRoles", key = "#userId")
    public void removeRoleFromUser(Long userId, String role) {
        UserRoleId userRoleId = new UserRoleId(userId, role);
        if (!userRoleRepository.existsById(userRoleId)) {
            throw new IllegalArgumentException("⚠️ У пользователя нет такой роли");
        }

        userRoleRepository.deleteById(userRoleId);
        LOGGER.info("✅ Роль '{}' удалена у пользователя '{}'", role, userId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("❌ Пользователь не найден");
        }

        userRepository.deleteById(userId);
        LOGGER.info("✅ Пользователь '{}' удален", userId);
    }

    @Cacheable(value = "allUsers")
    public List<String> getAllUsers() {
        return userRepository.findAll()
                .stream().map(User::getLogin).collect(Collectors.toList());
    }
}
