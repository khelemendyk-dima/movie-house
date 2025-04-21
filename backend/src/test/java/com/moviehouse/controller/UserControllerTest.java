package com.moviehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.UserDto;
import com.moviehouse.exception.UserNotFoundException;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        List<UserDto> users = List.of(
                TestDataFactory.createUserDto("USER"),
                TestDataFactory.createUserDto("ADMIN")
        );

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].role").value("USER"))
                .andExpect(jsonPath("$[1].role").value("ADMIN"));
    }

    @Test
    void getUserById_shouldReturnUser_whenExists() throws Exception {
        UserDto user = TestDataFactory.createUserDto("USER");

        Mockito.when(userService.getUserById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void getUserById_shouldReturn404_whenUserNotFound() throws Exception {
        Mockito.when(userService.getUserById(99L))
                .thenThrow(UserNotFoundException.class);

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser_whenValid() throws Exception {
        UserDto request = TestDataFactory.createUserDto("USER");
        request.setId(null);
        UserDto updated = TestDataFactory.createUserDto("USER");
        updated.setName("Updated");

        Mockito.when(userService.updateUser(1L, request)).thenReturn(updated);

        mockMvc.perform(put("/api/users/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void updateUser_shouldReturn400_whenInvalidUser() throws Exception {
        UserDto request = new UserDto();

        mockMvc.perform(put("/api/users/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_shouldReturn404_whenNotFound() throws Exception {
        UserDto request = TestDataFactory.createUserDto("USER");

        Mockito.when(userService.updateUser(42L, request))
                .thenThrow(UserNotFoundException.class);

        mockMvc.perform(put("/api/users/42")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturnDeletedUser_whenExists() throws Exception {
        UserDto deleted = TestDataFactory.createUserDto("USER");

        Mockito.when(userService.deleteUser(1L)).thenReturn(deleted);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void deleteUser_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(userService.deleteUser(100L))
                .thenThrow(UserNotFoundException.class);

        mockMvc.perform(delete("/api/users/100"))
                .andExpect(status().isNotFound());
    }
}
