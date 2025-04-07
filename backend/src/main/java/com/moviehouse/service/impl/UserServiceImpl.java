package com.moviehouse.service.impl;

import com.moviehouse.dto.UserDto;
import com.moviehouse.exception.RoleNotFoundException;
import com.moviehouse.exception.UserNotFoundException;
import com.moviehouse.model.Role;
import com.moviehouse.model.User;
import com.moviehouse.repository.RoleRepository;
import com.moviehouse.repository.UserRepository;
import com.moviehouse.service.UserService;
import com.moviehouse.util.ConvertorUtil;
import com.moviehouse.util.UserValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserValidatorUtil userValidator;
    private final ConvertorUtil convertor;

    @Override
    public List<UserDto> getAllUsers() {
        log.info("Fetching all users");

        List<UserDto> users = userRepository.findAll().stream()
                .map(convertor::toUserDto)
                .toList();

        log.info("Found {} users", users.size());

        return users;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.info("Fetching user with id={}", id);

        User user = findUserById(id);

        log.info("User with id={} was found", user.getId());

        return convertor.toUserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        log.info("Fetching user with email={}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        log.info("User with email={} was found", user.getEmail());

        return convertor.toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        log.info("Updating user with id={}", id);

        userValidator.checkIfEmailInUse(id, userDto.getEmail());

        User userToUpdate = setupUserToUpdate(id, userDto);
        userRepository.save(userToUpdate);

        log.info("User with id={} updated successfully", id);

        return convertor.toUserDto(userToUpdate);
    }

    @Transactional
    @Override
    public UserDto deleteUser(Long id) {
        log.info("Deleting user with id={}", id);

        User user = findUserById(id);

        userRepository.delete(user);

        log.info("User with id={} deleted successfully", id);

        return convertor.toUserDto(user);
    }

    private User setupUserToUpdate(Long id, UserDto userDto) {
        log.debug("Setting up user with id={}", id);

        Role role = roleRepository.findByName(userDto.getRole())
                .orElseThrow(() -> new RoleNotFoundException(userDto.getRole()));

        User existingUser = findUserById(id);

        existingUser.setRole(role);
        existingUser.setName(userDto.getName());
        existingUser.setEmail(userDto.getEmail());

        return existingUser;
    }

    private User findUserById(Long id) {
        log.info("Searching user with id={}", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
