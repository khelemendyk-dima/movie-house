package com.moviehouse.util;

import com.moviehouse.exception.PasswordMismatchException;
import com.moviehouse.exception.UserAlreadyExistsException;
import com.moviehouse.model.User;
import com.moviehouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidatorUtil {
    private final UserRepository userRepository;

    public void validateEmailUniqueness(String email) {
        log.debug("Validating email uniqueness for user with email: '{}'", email);

        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(email);
        }
    }

    public void checkIfEmailInUse(Long id, String email) {
        log.debug("Checking if user with email: '{}' is already in use", email);

        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isPresent() && !Objects.equals(userByEmail.get().getId(), id)) {
            throw new UserAlreadyExistsException(email);
        }
    }

    public void validatePasswordMatching(String password, String confirmPassword) {
        log.debug("Validating password matching");

        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException();
        }
    }
}


