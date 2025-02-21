package com.example.demo.controllers;

import com.example.demo.domain.dto.ChangePasswordRequest;
import com.example.demo.services.AdminService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
            @RequestBody @Valid ChangePasswordRequest request) {
        userService.changePassword(authentication.getName(), request.getOldPassword(), request.getNewPassword());
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