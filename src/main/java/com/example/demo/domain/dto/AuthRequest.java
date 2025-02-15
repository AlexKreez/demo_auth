package com.example.demo.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequest {
    private String login;
    private String password;
    private String email;
}
