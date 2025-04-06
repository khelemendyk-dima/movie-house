package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.ROLE_BY_NAME_NOT_FOUND;
import static java.lang.String.format;

public class RoleNotFoundException extends NotFoundException {

    public RoleNotFoundException(String role) {
        super(format(ROLE_BY_NAME_NOT_FOUND, role));
    }
}
