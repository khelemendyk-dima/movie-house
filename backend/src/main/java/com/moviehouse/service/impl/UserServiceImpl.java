package com.moviehouse.service.impl;

import com.moviehouse.dto.UserDto;
import com.moviehouse.exceptions.RoleNotFoundException;
import com.moviehouse.exceptions.UserNotFoundException;
import com.moviehouse.model.Role;
import com.moviehouse.model.User;
import com.moviehouse.repository.RoleRepository;
import com.moviehouse.repository.UserRepository;
import com.moviehouse.service.UserService;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.util.UserValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidatorUtil userValidator;
    private final ConvertorUtil convertor;

    @Transactional
    @Override
    public UserDto update(Long id, UserDto userDto) {
        userValidator.checkIfEmailInUse(id, userDto.getEmail());

        User userToUpdate = setupUserToUpdate(id, userDto);
        userRepository.save(userToUpdate);

        return convertor.toUserDto(userToUpdate);
    }

    @Transactional
    @Override
    public UserDto delete(Long id) {
        User user = findUserById(id);

        userRepository.delete(user);

        return convertor.toUserDto(user);
    }

    @Override
    public UserDto getById(Long id) {
        User user = findUserById(id);

        return convertor.toUserDto(user);
    }

    @Override
    public UserDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return convertor.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(convertor::toUserDto)
                .collect(Collectors.toList());
    }

    private User setupUserToUpdate(Long id, UserDto userDto) {
        Role role = roleRepository.findByName(userDto.getRole())
                .orElseThrow(() -> new RoleNotFoundException(userDto.getRole()));

        User existingUser = findUserById(id);

        existingUser.setRole(role);
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());

        return existingUser;
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
