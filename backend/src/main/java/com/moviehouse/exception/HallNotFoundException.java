package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.HALL_BY_ID_NOT_FOUND;
import static java.lang.String.format;

public class HallNotFoundException extends NotFoundException {
    public HallNotFoundException(Long id) {
        super(format(HALL_BY_ID_NOT_FOUND, id));
    }
}
