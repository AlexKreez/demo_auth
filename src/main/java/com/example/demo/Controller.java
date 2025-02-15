package com.example.demo;

import com.example.demo.domain.dto.AuthRequest;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class Controller {

    private final UserService userService;

    /**
     * 📌 Регистрация нового пользователя
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest request) {
        userService.registerUser(request.getLogin(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok("User registered successfully");
    }

    /**
     * 📌 Логин и генерация JWT токена
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        String token = userService.authenticateUser(request.getLogin(), request.getPassword());
        return ResponseEntity.ok(token);
    }
}
