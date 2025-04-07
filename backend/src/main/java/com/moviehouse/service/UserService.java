package com.moviehouse.service;

import com.moviehouse.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto getUserByEmail(String email);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto deleteUser(Long id);
}
