package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMAIL_ALREADY_EXISTS;
import static java.lang.String.format;

public class UserAlreadyExistsException extends AlreadyExistsException {
    public UserAlreadyExistsException(String email) {
        super(format(EMAIL_ALREADY_EXISTS, email));
    }
}
