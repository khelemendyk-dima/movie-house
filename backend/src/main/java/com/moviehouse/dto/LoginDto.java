package com.moviehouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank(message = EMPTY_EMAIL)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD)
    private String password;
}