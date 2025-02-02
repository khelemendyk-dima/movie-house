package com.moviehouse.exceptions;

public class PasswordMismatchException extends ServiceException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}
