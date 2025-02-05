package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.EMAIL_ALREADY_EXISTS;
import static java.lang.String.format;

public class UserAlreadyExistsException extends AlreadyExistsException {
    public UserAlreadyExistsException(String email) {
        super(format(EMAIL_ALREADY_EXISTS, email));
    }
}
