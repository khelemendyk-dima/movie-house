package com.moviehouse.exception;

public class AlreadyExistsException extends ServiceException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
