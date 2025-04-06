package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.PASSWORD_MISMATCH;

public class PasswordMismatchException extends ServiceException {
    public PasswordMismatchException() {
        super(PASSWORD_MISMATCH);
    }
}
