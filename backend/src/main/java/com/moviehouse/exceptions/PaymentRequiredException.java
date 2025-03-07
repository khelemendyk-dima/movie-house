package com.moviehouse.exceptions;

public class PaymentRequiredException extends ServiceException {

    public PaymentRequiredException(String message) {
        super(message);
    }
}
