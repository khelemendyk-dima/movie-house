package com.moviehouse.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthDto {
    private String token;
    private UserDto user;
}

