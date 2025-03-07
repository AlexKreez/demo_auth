package com.example.demo.repository;

import com.example.demo.domain.entityUser.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
//    boolean existsByLogin(String login);
//    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}

