package com.example.demo.services;


import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.domain.entityUser.UserRoleId;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public List<String> getUserRolesByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("❌ Пользователь не найден"));
        return userRoleRepository.findByUserId(user.getId())
                .stream().map(UserRole::getRole).collect(Collectors.toList());
    }

    @Transactional
    public void addRoleToUser(Long userId, String role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("❌ Пользователь не найден"));

        if (userRoleRepository.findById(new UserRoleId(userId, role)).isPresent()) {
            throw new IllegalArgumentException("⚠️ Роль уже назначена пользователю");
        }

        UserRole userRole = new UserRole(user, role);
        userRoleRepository.save(userRole);
        System.out.println("✅ Роль " + role + " добавлена пользователю " + userId);
    }

    @Transactional
    public void removeRoleFromUser(Long userId, String role) {
        UserRoleId userRoleId = new UserRoleId(userId, role);
        if (!userRoleRepository.existsById(userRoleId)) {
            throw new IllegalArgumentException("⚠️ У пользователя нет такой роли");
        }

        userRoleRepository.deleteById(userRoleId);
        System.out.println("✅ Роль " + role + " удалена у пользователя " + userId);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("❌ Пользователь не найден");
        }

        userRepository.deleteById(userId);
        System.out.println("✅ Пользователь " + userId + " удален");
    }

    public List<String> getAllUsers() {
        return userRepository.findAll()
                .stream().map(User::getLogin).collect(Collectors.toList());
    }
}
