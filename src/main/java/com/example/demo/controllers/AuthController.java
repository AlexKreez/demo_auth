package com.example.demo.controllers;

import com.example.demo.domain.dto.AuthRequest;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 📌 Регистрация нового пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody AuthRequest request) {
        userService.registerUser(request.getLogin(),  request.getEmail());
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }
    /**
     * 📌 Логин и генерация JWT токена
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequest request) {
        String token = userService.authenticateUser(request.getLogin(), request.getPassword());
        return ResponseEntity.ok(Map.of("token: ", token));
    }
}
