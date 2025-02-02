package com.moviehouse.util;

import com.moviehouse.exceptions.PasswordMismatchException;
import com.moviehouse.exceptions.UserAlreadyExistsException;
import com.moviehouse.model.User;
import com.moviehouse.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.EMAIL_ALREADY_EXISTS;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.PASSWORD_MISMATCH;
import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class UserValidatorUtil {
    private final UserRepository userRepository;

    public void validateEmailUniqueness(String email) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException(String.format(EMAIL_ALREADY_EXISTS, email));
        }
    }

    public void checkIfEmailInUse(Long id, String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);

        if (userByEmail.isPresent() && !Objects.equals(userByEmail.get().getId(), id)) {
            throw new UserAlreadyExistsException(format(EMAIL_ALREADY_EXISTS, email));
        }
    }

    public void validatePasswordMatching(String password, String confirmPassword) throws PasswordMismatchException {
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }
    }
}


