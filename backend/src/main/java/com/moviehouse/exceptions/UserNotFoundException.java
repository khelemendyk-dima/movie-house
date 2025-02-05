package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.USER_BY_EMAIL_NOT_FOUND;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.USER_BY_ID_NOT_FOUND;
import static java.lang.String.format;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String email) {
        super(format(USER_BY_EMAIL_NOT_FOUND, email));
    }

    public UserNotFoundException(Long id) {
        super(format(USER_BY_ID_NOT_FOUND, id));
    }
}
