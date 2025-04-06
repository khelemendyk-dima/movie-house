package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.BOOKING_ALREADY_PAID;
import static java.lang.String.format;

public class BookingAlreadyPaidException extends AlreadyExistsException {

    public BookingAlreadyPaidException(Long bookingId) {
        super(format(BOOKING_ALREADY_PAID, bookingId));
    }
}
