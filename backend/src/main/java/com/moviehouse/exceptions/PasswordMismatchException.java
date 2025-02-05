package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.PASSWORD_MISMATCH;

public class PasswordMismatchException extends ServiceException {
    public PasswordMismatchException() {
        super(PASSWORD_MISMATCH);
    }
}
