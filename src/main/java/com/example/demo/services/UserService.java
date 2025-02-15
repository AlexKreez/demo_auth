package com.example.demo.services;

import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public User registerUser(String login, String password, String email) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        User savedUser = userRepository.save(user);
        System.out.println("✅ User registered with ID: " + savedUser.getId());

        UserRole userRole = new UserRole(savedUser, "USER");
        userRoleRepository.save(userRole);
        System.out.println("✅ Role USER assigned to: " + savedUser.getLogin());

        return savedUser;
    }

    public String authenticateUser(String login, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return jwtService.generateToken(user);
    }
}
