package com.example.demo.services;

import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public User registerUser(String login, String email) {

        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("❌ Пользователь уже существует");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("❌ Email уже используется");
        }
        String generatedPassword = PasswordGenerator.generateRandomPassword(12);
        String hashedPassword = passwordEncoder.encode(generatedPassword);

        User user = new User();
        user.setLogin(login);
        user.setPassword(hashedPassword);
        user.setEmail(email);
        User savedUser = userRepository.save(user);

        System.out.println("✅ Пользователь зарегистрирован с ID: " + savedUser.getId());
        UserRole userRole = new UserRole(savedUser, "USER");

        userRoleRepository.save(userRole);
        System.out.println("✅ Роль USER выдана: " + savedUser.getLogin());

        mailService.sendEmail(email, generatedPassword);
        return savedUser;
    }

    public String authenticateUser(String login, String password) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("❌ Ошибка: Пользователь не найден"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.error("❌ Ошибка: Неверный пароль для пользователя {}", login);
            throw new BadCredentialsException("Неверный пароль");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );
        return jwtService.generateToken(user);
    }


    public void changePassword(String login, String oldPassword, String newPassword) {
        changePasswordInternal(login, oldPassword, newPassword);
    }

    private void changePasswordInternal(String login, String oldPassword, String newPassword) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("❌ Пользователь не найден"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            LOGGER.warn("⚠️ Неверный текущий пароль для пользователя {}", login);
            throw new BadCredentialsException("❌ Неверный текущий пароль");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        LOGGER.info("✅ Пароль успешно изменен для пользователя {}", login);
    }
}
