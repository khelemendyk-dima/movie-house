package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.BOOKING_ALREADY_PAID;
import static java.lang.String.format;

public class BookingAlreadyPaidException extends AlreadyExistsException {

    public BookingAlreadyPaidException(Long bookingId) {
        super(format(BOOKING_ALREADY_PAID, bookingId));
    }
}
