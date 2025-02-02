package com.moviehouse.service.impl;

import com.moviehouse.dto.AuthDto;
import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.UserDto;
import com.moviehouse.exceptions.UserNotFoundException;
import com.moviehouse.model.User;
import com.moviehouse.repository.UserRepository;
import com.moviehouse.service.AuthService;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.util.JwtUtil;
import com.moviehouse.util.UserValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.USER_BY_EMAIL_NOT_FOUND;
import static java.lang.String.format;

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
        userValidator.validatePasswordMatching(registrationDto.getPassword(), registrationDto.getConfirmPassword());
        userValidator.validateEmailUniqueness(registrationDto.getEmail());

        User userToRegister = convertor.toUser(registrationDto);
        userRepository.save(userToRegister);

        return convertor.toUserDto(userToRegister);
    }

    @Override
    public AuthDto authenticate(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                ));

        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException(format(USER_BY_EMAIL_NOT_FOUND, loginDto.getEmail())));

        return AuthDto.builder()
                .token(jwtUtil.generateToken(user))
                .user(convertor.toUserDto(user))
                .build();
    }
}
