package com.moviehouse.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.NAME_SIZE_EXCEEDED;
import static com.moviehouse.util.constant.RegexConstant.NAME_REGEX;

@Data
public class UserDto {
    private Long id;

    @NotBlank(message = EMPTY_ROLE)
    private String role;

    @Pattern(regexp = NAME_REGEX, message = INCORRECT_NAME_FORMAT)
    @NotBlank(message = EMPTY_NAME)
    @Size(max = 50, message = NAME_SIZE_EXCEEDED)
    private String name;

    @NotBlank(message = EMPTY_EMAIL)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;
}
