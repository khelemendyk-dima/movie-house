package com.moviehouse.exception;

import java.util.List;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.SEATS_ALREADY_BOOKED;
import static java.lang.String.format;

public class SeatAlreadyBookedException extends AlreadyExistsException {
    public SeatAlreadyBookedException(List<Long> seatIds) {
        super(format(SEATS_ALREADY_BOOKED, seatIds));
    }
}
