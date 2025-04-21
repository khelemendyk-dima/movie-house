package com.moviehouse.service.impl;

import com.moviehouse.dto.AuthDto;
import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.UserDto;
import com.moviehouse.exception.PasswordMismatchException;
import com.moviehouse.exception.UserAlreadyExistsException;
import com.moviehouse.exception.UserNotFoundException;
import com.moviehouse.model.Role;
import com.moviehouse.model.User;
import com.moviehouse.repository.UserRepository;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.util.JwtUtil;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.util.UserValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidatorUtil userValidator;

    @Mock
    private ConvertorUtil convertor;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegistrationDto registrationDto;
    private LoginDto loginDto;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        registrationDto = TestDataFactory.createRegistrationDto();
        loginDto = TestDataFactory.createLoginDto();

        Role role = TestDataFactory.createRole("USER");
        user = TestDataFactory.createUser(role);

        userDto = TestDataFactory.createUserDto(role.getName());
    }

    @Test
    void register_shouldSaveUserAndReturnUserDto() {
        when(convertor.toUser(registrationDto)).thenReturn(user);
        when(convertor.toUserDto(user)).thenReturn(userDto);

        UserDto result = authService.register(registrationDto);

        assertEquals(userDto, result);

        verify(userValidator).validatePasswordMatching(registrationDto.getPassword(), registrationDto.getConfirmPassword());
        verify(userValidator).validateEmailUniqueness(registrationDto.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void register_shouldThrowException_whenPasswordNotMatching() {
        registrationDto.setPassword("secretPassword");
        registrationDto.setConfirmPassword("mismatchPassword");

        doThrow(PasswordMismatchException.class)
                .when(userValidator).validatePasswordMatching(registrationDto.getPassword(), registrationDto.getConfirmPassword());

        assertThrows(PasswordMismatchException.class, () -> authService.register(registrationDto));

        verify(userValidator).validatePasswordMatching(registrationDto.getPassword(), registrationDto.getConfirmPassword());
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_shouldThrowException_whenUserAlreadyExists() {
        doThrow(UserAlreadyExistsException.class)
                .when(userValidator).validateEmailUniqueness(registrationDto.getEmail());

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(registrationDto));

        verify(userValidator).validateEmailUniqueness(registrationDto.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void authenticate_shouldAuthenticateAndReturnAuthDto() {
        String expectedToken = "jwt-token";

        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn(expectedToken);
        when(convertor.toUserDto(user)).thenReturn(userDto);

        AuthDto result = authService.authenticate(loginDto);

        assertEquals(expectedToken, result.getToken());
        assertEquals(userDto, result.getUser());

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        verify(userRepository).findByEmail(loginDto.getEmail());
        verify(jwtUtil).generateToken(user);
    }

    @Test
    void authenticate_shouldThrowUserNotFoundException_whenUserNotFound() {
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.authenticate(loginDto));

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        verify(userRepository, never()).save(any());
    }
}
