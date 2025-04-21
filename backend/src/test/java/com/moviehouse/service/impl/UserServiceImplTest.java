package com.moviehouse.service.impl;

import com.moviehouse.dto.UserDto;
import com.moviehouse.exception.RoleNotFoundException;
import com.moviehouse.exception.UserAlreadyExistsException;
import com.moviehouse.exception.UserNotFoundException;
import com.moviehouse.model.Role;
import com.moviehouse.model.User;
import com.moviehouse.repository.RoleRepository;
import com.moviehouse.repository.UserRepository;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.util.UserValidatorUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ConvertorUtil convertor;

    @Mock
    private UserValidatorUtil userValidator;

    @InjectMocks
    private UserServiceImpl userService;

    private Role role;
    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        role = TestDataFactory.createRole("USER");
        user = TestDataFactory.createUser(role);

        userDto = TestDataFactory.createUserDto(role.getName());
    }

    @Test
    void getAllUsers_shouldReturnListOfUserDtos() {
        List<User> users = List.of(user, user);
        List<UserDto> userDtos = List.of(userDto, userDto);

        when(userRepository.findAll()).thenReturn(users);
        when(convertor.toUserDto(any(User.class))).thenReturn(userDto);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(userDtos.size(), result.size());
        assertArrayEquals(userDtos.toArray(), result.toArray());

        verify(userRepository).findAll();
        verify(convertor, times(2)).toUserDto(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnEmptyList_whenNoUsers() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> result = userService.getAllUsers();

        assertEquals(0, result.size());

        verify(userRepository).findAll();
        verify(convertor, never()).toUserDto(any(User.class));
    }

    @Test
    void getUserById_shouldReturnUserDto_whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(convertor.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserById(1L);

        assertEquals(userDto, result);

        verify(userRepository).findById(1L);
        verify(convertor).toUserDto(user);
    }

    @Test
    void getUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(404L));
    }

    @Test
    void getUserByEmail_shouldReturnUserDto_whenUserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(convertor.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.getUserByEmail(user.getEmail());

        assertEquals(userDto, result);

        verify(userRepository).findByEmail(user.getEmail());
        verify(convertor).toUserDto(any(User.class));
    }

    @Test
    void getUserByEmail_shouldThrowException_whenUserNotFound() {
        when(userRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("notfound@example.com"));
    }

    @Test
    void updateUser_shouldUpdateUserAndReturnDto_whenUserExists() {
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(convertor.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.updateUser(1L, userDto);

        assertEquals(userDto, result);

        verify(userRepository).save(user);
        verify(convertor).toUserDto(any(User.class));
    }

    @Test
    void updateUser_shouldThrowException_whenEmailInUse() {
        doThrow(UserAlreadyExistsException.class)
                .when(userValidator).checkIfEmailInUse(1L, userDto.getEmail());

        assertThrows(UserAlreadyExistsException.class, () -> userService.updateUser(1L, userDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldThrowException_whenRoleNotFound() {
        userDto.setRole("WRONG_ROLE");

        when(roleRepository.findByName("WRONG_ROLE")).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> userService.updateUser(1L, userDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_shouldThrowException_whenUserNotFound() {
        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, userDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_shouldDeleteUserAndReturnDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(convertor.toUserDto(user)).thenReturn(userDto);

        UserDto result = userService.deleteUser(1L);

        assertEquals(userDto, result);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUser_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(404L));

        verify(userRepository, never()).delete(any());
    }
}
