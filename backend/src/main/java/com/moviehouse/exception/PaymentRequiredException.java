package com.moviehouse.exception;

public class PaymentRequiredException extends ServiceException {

    public PaymentRequiredException(String message) {
        super(message);
    }
}
