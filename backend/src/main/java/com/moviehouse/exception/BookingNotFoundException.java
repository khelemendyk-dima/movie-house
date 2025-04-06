package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.BOOKING_BY_ID_NOT_FOUND;
import static java.lang.String.format;

public class BookingNotFoundException extends NotFoundException {
    public BookingNotFoundException(Long id) {
        super(format(BOOKING_BY_ID_NOT_FOUND, id));
    }
}
