package com.example.demo.services;

import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import com.example.demo.security.jwt.JwtService;
import com.example.demo.utils.PasswordGenerator;
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
    private final MailService mailService;

    @Transactional //одна попытка на выполнение
    public User registerUser(String login, String email) {
        if (userRepository.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("❌ Пользователь уже существует");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("❌ Email уже используется");
        }
        //Генерируем пароль
        String generatedPassword = PasswordGenerator.generateRandomPassword(12);

        //Хешируем его перед сохранением
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

        //ОТправляем пароль по email
        mailService.sendEmail(email, generatedPassword);

        return savedUser;
    }

    public String authenticateUser(String login, String password) {
        System.out.println("🔍 Попытка входа: " + login);

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("❌ Ошибка: Пользователь не найден"));

//        //  Если пользователь admin, проверяем, есть ли у него токен
//        if ("admin".equals(login)) {
//            String existingToken = jwtService.findExistingTokenForUser(user);
//            if (existingToken != null) {
//                System.out.println("✅ У `admin` уже есть активный токен, возвращаем его");
//                return existingToken;
//            }
//        }

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
