package com.example.demo;

import com.example.demo.domain.entityUser.User;
import com.example.demo.domain.entityUser.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByLogin("admin").isEmpty()) {
            System.out.println("🔹 Администратор не найден, создаём нового...");

            User admin = new User();
            admin.setLogin("admin");
            admin.setEmail("admin@x.ru");
            admin.setPassword(passwordEncoder.encode("admin")); // ✅ Шифруем пароль перед сохранением

            admin = userRepository.save(admin);
            System.out.println("✅ Администратор создан: " + admin.getLogin());

            // Добавляем роли
            userRoleRepository.save(new UserRole(admin, "ADMIN"));
            userRoleRepository.save(new UserRole(admin, "USER"));

            System.out.println("✅ Роли ADMIN и USER назначены администратору.");
        } else {
            System.out.println("✅ Администратор уже существует.");
        }
    }
}
