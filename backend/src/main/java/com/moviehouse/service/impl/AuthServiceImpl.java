package com.moviehouse.service.impl;

import com.moviehouse.dto.AuthDto;
import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.UserDto;
import com.moviehouse.exception.UserNotFoundException;
import com.moviehouse.model.User;
import com.moviehouse.repository.UserRepository;
import com.moviehouse.service.AuthService;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.util.JwtUtil;
import com.moviehouse.util.UserValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserValidatorUtil userValidator;
    private final ConvertorUtil convertor;
    private final JwtUtil jwtUtil;

    @Transactional
    @Override
    public UserDto register(RegistrationDto registrationDto) {
        log.info("Registering new user with email: '{}'", registrationDto.getEmail());

        userValidator.validatePasswordMatching(registrationDto.getPassword(), registrationDto.getConfirmPassword());
        userValidator.validateEmailUniqueness(registrationDto.getEmail());

        User userToRegister = convertor.toUser(registrationDto);
        userRepository.save(userToRegister);

        log.info("User registered successfully with email: '{}'", registrationDto.getEmail());

        return convertor.toUserDto(userToRegister);
    }

    @Override
    public AuthDto authenticate(LoginDto loginDto) {
        log.info("Authenticating user with email: '{}'", loginDto.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                ));

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(loginDto.getEmail()));

        String token = jwtUtil.generateToken(user);
        log.info("User authenticated successfully: {}", loginDto.getEmail());

        return AuthDto.builder()
                .token(token)
                .user(convertor.toUserDto(user))
                .build();
    }
}
