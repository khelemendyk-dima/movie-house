package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.ROLE_BY_NAME_NOT_FOUND;
import static java.lang.String.format;

public class RoleNotFoundException extends NotFoundException {

    public RoleNotFoundException(String role) {
        super(format(ROLE_BY_NAME_NOT_FOUND, role));
    }
}
