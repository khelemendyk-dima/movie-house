package com.moviehouse.service;

import com.moviehouse.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto getById(Long id);

    UserDto getByEmail(String email);

    UserDto update(Long id, UserDto userDto);

    UserDto delete(Long id);
}
