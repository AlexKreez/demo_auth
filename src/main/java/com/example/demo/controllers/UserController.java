package com.example.demo.controllers;

import com.example.demo.services.AdminService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AdminService adminService;

    /**
     * Смена пароля (доступно только самому пользователю)
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @RequestBody Map<String, String> passwordData) { // Принимаем JSON

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");
        System.out.println("🔍 Полученные данные: " + passwordData);
        System.out.println("🔍 oldPassword = " + passwordData.get("oldPassword"));
        System.out.println("🔍 newPassword = " + passwordData.get("newPassword"));

        System.out.println("🔍 UserService в UserController перед вызовом changePassword(): " + userService);
        System.out.println("🔍 Контроллерный userService: " + userService);
        System.out.println("🔍 Контекст Spring Security: " + SecurityContextHolder.getContext());


        userService.changePassword(authentication.getName(), oldPassword, newPassword);
        return ResponseEntity.ok("Пароль успешно изменен");
    }


    /**
     * Получить список всех пользователей (Только ADMIN)
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<String>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }
}