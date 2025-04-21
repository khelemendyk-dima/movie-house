package com.moviehouse.util;

import com.moviehouse.exception.PasswordMismatchException;
import com.moviehouse.exception.UserAlreadyExistsException;
import com.moviehouse.model.User;
import com.moviehouse.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorUtilTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidatorUtil userValidatorUtil;

    @Test
    void validateEmailUniqueness_shouldNotThrowException_whenEmailNotInUse() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> userValidatorUtil.validateEmailUniqueness(email));
    }

    @Test
    void validateEmailUniqueness_shouldThrowException_whenEmailExists() {
        String email = "existing@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userValidatorUtil.validateEmailUniqueness(email));
    }

    @Test
    void checkIfEmailInUse_shouldNotThrowException_whenEmailNotInUse() {
        String email = "free@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userValidatorUtil.checkIfEmailInUse(1L, email));
    }

    @Test
    void checkIfEmailInUse_shouldNotThrowException_whenEmailBelongsToSameUser() {
        String email = "sameuser@example.com";
        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userValidatorUtil.checkIfEmailInUse(1L, email));
    }

    @Test
    void checkIfEmailInUse_shouldThrowException_whenEmailInUseByAnotherUser() {
        String email = "used@example.com";
        User otherUser = new User();
        otherUser.setId(2L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(otherUser));

        assertThrows(UserAlreadyExistsException.class, () -> userValidatorUtil.checkIfEmailInUse(1L, email));
    }

    @Test
    void validatePasswordMatching_shouldNotThrowException_whenPasswordsMatch() {
        String password = "secure123";

        assertDoesNotThrow(() -> userValidatorUtil.validatePasswordMatching(password, password));
    }

    @Test
    void validatePasswordMatching_shouldThrowException_whenPasswordsDoNotMatch() {
        String password = "secure123";
        String confirmPassword = "secure321";

        assertThrows(PasswordMismatchException.class,
                () -> userValidatorUtil.validatePasswordMatching(password, confirmPassword));
    }
}
