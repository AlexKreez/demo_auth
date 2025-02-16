package com.example.demo.services;

import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
            throw new IllegalArgumentException("Пользователь уже существует");
        }

        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);

        User savedUser = userRepository.save(user);
        System.out.println("✅ Пользователь зарегистрирован с ID: " + savedUser.getId());

        UserRole userRole = new UserRole(savedUser, "USER");
        userRoleRepository.save(userRole);
        System.out.println("✅ Роль USER выдана: " + savedUser.getLogin());

        return savedUser;
    }

    public String authenticateUser(String login, String password) {
        System.out.println("🔍 Попытка входа: " + login);



        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("❌ Ошибка: Пользователь не найден"));


        // ✅ Если пользователь admin, проверяем, есть ли у него токен
        if ("admin".equals(login)) {
            String existingToken = jwtService.findExistingTokenForUser(user);
            if (existingToken != null) {
                System.out.println("✅ У `admin` уже есть активный токен, возвращаем его");
                return existingToken;
            }
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            System.out.println("❌ Ошибка: Неверный пароль для пользователя " + login);
            throw new BadCredentialsException("Неверный пароль");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );

        return jwtService.generateToken(user);
    }
}
