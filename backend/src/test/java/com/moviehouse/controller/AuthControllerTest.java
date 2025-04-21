package com.moviehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.AuthDto;
import com.moviehouse.dto.LoginDto;
import com.moviehouse.dto.RegistrationDto;
import com.moviehouse.dto.UserDto;
import com.moviehouse.exception.UserAlreadyExistsException;
import com.moviehouse.exception.UserNotFoundException;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.AuthService;
import com.moviehouse.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser_shouldReturnUserDto() throws Exception {
        RegistrationDto registrationDto = TestDataFactory.createRegistrationDto();
        UserDto userDto = TestDataFactory.createUserDto("USER");

        Mockito.when(authService.register(any())).thenReturn(userDto);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void registerUser_shouldReturn400_whenInvalidEmail() throws Exception {
        RegistrationDto invalidDto = TestDataFactory.createRegistrationDto();
        invalidDto.setEmail("not-an-email");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registerUser_shouldReturn409_whenWithExistingEmail() throws Exception {
        RegistrationDto registrationDto = TestDataFactory.createRegistrationDto();

        Mockito.when(authService.register(any()))
                .thenThrow(UserAlreadyExistsException.class);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isConflict());
    }


    @Test
    void authenticateUser_shouldReturnTokeAndUser() throws Exception {
        LoginDto loginDto = TestDataFactory.createLoginDto();
        AuthDto authDto = TestDataFactory.createAuthDto();

        Mockito.when(authService.authenticate(any())).thenReturn(authDto);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.token").value("mock-token"))
                .andExpect(cookie().value("jwt", "mock-token"));
    }

    @Test
    void authenticateUser_shouldReturn400_whenInvalidLogin() throws Exception {
        LoginDto loginDto = new LoginDto();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticateUser_shouldReturn401_whenInvalidCredentials() throws Exception {
        LoginDto loginDto = TestDataFactory.createLoginDto();

        Mockito.when(authService.authenticate(any()))
                .thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logout_shouldClearCookie() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(cookie().maxAge("jwt", 0));
    }

    @Test
    @WithMockUser(username = "john.doe@example.com")
    void getCurrentUser_shouldReturnUserDto() throws Exception {
        UserDto userDto = TestDataFactory.createUserDto("USER");

        Mockito.when(userService.getUserByEmail("john.doe@example.com")).thenReturn(userDto);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "unknown@example.com")
    void getCurrentUser_shouldReturn404_whenEmailNotFound() throws Exception {
        Mockito.when(userService.getUserByEmail("unknown@example.com")).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCurrentUser_shouldReturn401_whenNoUserProvided() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
