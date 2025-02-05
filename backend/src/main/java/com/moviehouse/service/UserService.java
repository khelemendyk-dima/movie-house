package com.moviehouse.service;

import com.moviehouse.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto update(Long id, UserDto userDto);
    UserDto delete(Long id);
    UserDto getById(Long id);
    List<UserDto> getAll();
}
