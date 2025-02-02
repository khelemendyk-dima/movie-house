package com.moviehouse.service;

import com.moviehouse.dto.UserDto;
import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.AuthDto;

public interface AuthService {
    UserDto register(RegistrationDto registrationDto);
    AuthDto authenticate(LoginDto loginDto);
}
