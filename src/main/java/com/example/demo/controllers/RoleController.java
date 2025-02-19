package com.example.demo.controllers;

import com.example.demo.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final AdminService adminService;

    /**
     * Получить список ролей пользователя по логину (Только ADMIN)
     */
    @GetMapping("/by-login/{login}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<String>> getUserRoles(@PathVariable String login) {
        return ResponseEntity.ok(adminService.getUserRolesByLogin(login));
    }

    /**
     * Назначить новую роль пользователю (Только ADMIN)
     */
    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addRoleToUser(@PathVariable Long userId, @RequestParam String role) {
        adminService.addRoleToUser(userId, role);
        return ResponseEntity.ok("Роль " + role + " добавлена пользователю " + userId);
    }

    /**
     * Удалить конкретную роль у пользователя (Только ADMIN)
     */
    @DeleteMapping("/{userId}/{role}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> removeRoleFromUser(@PathVariable Long userId, @PathVariable String role) {
        adminService.removeRoleFromUser(userId, role);
        return ResponseEntity.ok("Роль " + role + " удалена у пользователя " + userId);
    }

    /**
     * Удалить пользователя полностью (Только ADMIN)
     */
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok("Пользователь " + userId + " удален");
    }

}
