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

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.ROLE_BY_NAME_NOT_FOUND;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.USER_BY_ID_NOT_FOUND;
import static java.lang.String.format;

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
    public UserDto update(UserDto userDto) {
        userValidator.checkIfEmailInUse(userDto.getId(), userDto.getEmail());

        User existingUser = setupUserToUpdate(userDto);

        userRepository.save(existingUser);

        return convertor.toUserDto(existingUser);
    }

    @Transactional
    @Override
    public UserDto delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(format(USER_BY_ID_NOT_FOUND, userId)));

        userRepository.delete(user);

        return convertor.toUserDto(user);
    }

    @Override
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(format(USER_BY_ID_NOT_FOUND, id)));

        return convertor.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(convertor::toUserDto)
                .collect(Collectors.toList());
    }

    private User setupUserToUpdate(UserDto userDto) {
        Role role = roleRepository.findByName(userDto.getRole())
                .orElseThrow(() -> new RoleNotFoundException(format(ROLE_BY_NAME_NOT_FOUND, userDto.getRole())));

        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException(format(USER_BY_ID_NOT_FOUND, userDto.getId())));

        existingUser.setRole(role);
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());

        return existingUser;
    }
}
