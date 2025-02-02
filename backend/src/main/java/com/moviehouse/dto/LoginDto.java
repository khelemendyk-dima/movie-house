package com.moviehouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.*;

@Data
public class LoginDto {
    @NotBlank(message = EMPTY_EMAIL)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD)
    private String password;
}