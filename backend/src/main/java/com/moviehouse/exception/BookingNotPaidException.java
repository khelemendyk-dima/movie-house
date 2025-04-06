package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.BOOKING_NOT_PAID;
import static java.lang.String.format;

public class BookingNotPaidException extends PaymentRequiredException {
    public BookingNotPaidException(Long bookingId) {
        super(format(BOOKING_NOT_PAID, bookingId));
    }
}
