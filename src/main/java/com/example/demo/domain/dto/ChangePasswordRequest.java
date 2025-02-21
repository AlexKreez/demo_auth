package com.example.demo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    @NotBlank(message = "Старый пароль не должен быть пустым")
    private String oldPassword;

    @NotBlank(message = "Новый пароль не должен быть пустым")
    private String newPassword;
}
