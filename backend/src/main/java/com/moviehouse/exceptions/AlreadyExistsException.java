package com.moviehouse.exceptions;

public class AlreadyExistsException extends ServiceException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
