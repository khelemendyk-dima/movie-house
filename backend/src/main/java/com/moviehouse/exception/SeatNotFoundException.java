package com.moviehouse.exception;

import java.util.List;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.SEAT_BY_IDS_NOT_FOUND;
import static java.lang.String.format;

public class SeatNotFoundException extends NotFoundException {

    public SeatNotFoundException(List<Long> seatIds) {
        super(format(SEAT_BY_IDS_NOT_FOUND, seatIds));
    }
}