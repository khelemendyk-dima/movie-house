package com.moviehouse.service;

import com.moviehouse.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto update(UserDto userDto);
    UserDto delete(Long userId);
    UserDto getById(Long id);
    List<UserDto> getAll();
}
