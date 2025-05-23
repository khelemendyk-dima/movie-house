package com.moviehouse.exception;

import java.util.List;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.INVALID_SEATS_FOR_HALL;
import static java.lang.String.format;

public class InvalidSeatForHallException extends ServiceException {
    public InvalidSeatForHallException(Long sessionId, Long hallId, List<Long> seatIds) {
        super(format(INVALID_SEATS_FOR_HALL, sessionId, hallId, seatIds));
    }
}
