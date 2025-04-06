package com.moviehouse.exception;

public class ServiceException  extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }
}
