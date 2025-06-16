package com.moviehouse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;
import static com.moviehouse.util.constant.RegexConstant.NAME_REGEX;
import static com.moviehouse.util.constant.RegexConstant.PASSWORD_REGEX;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDto {
    @Pattern(regexp = NAME_REGEX, message = INCORRECT_NAME_FORMAT)
    @NotBlank(message = EMPTY_NAME)
    @Size(max = 50, message = NAME_SIZE_EXCEEDED)
    private String name;

    @NotBlank(message = EMPTY_EMAIL)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;

    @NotBlank(message = EMPTY_PASSWORD)
    @Pattern(regexp = PASSWORD_REGEX, message = INCORRECT_PASSWORD_FORMAT)
    private String password;

    @NotBlank(message = EMPTY_PASSWORD)
    @Pattern(regexp = PASSWORD_REGEX, message = INCORRECT_PASSWORD_FORMAT)
    private String confirmPassword;
}