package com.moviehouse.exceptions;

import java.util.List;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.SEATS_ALREADY_BOOKED;
import static java.lang.String.format;

public class SeatAlreadyBookedException extends AlreadyExistsException {
    public SeatAlreadyBookedException(List<Long> seatIds) {
        super(format(SEATS_ALREADY_BOOKED, seatIds));
    }
}
