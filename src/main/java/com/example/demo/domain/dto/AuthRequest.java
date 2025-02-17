package com.example.demo.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @Email
    private String email;
}
