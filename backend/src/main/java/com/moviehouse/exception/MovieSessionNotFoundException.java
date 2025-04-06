package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.MOVIE_SESSION_BY_ID_NOT_FOUND;
import static java.lang.String.format;

public class MovieSessionNotFoundException extends NotFoundException {
    public MovieSessionNotFoundException(Long id) {
        super(format(MOVIE_SESSION_BY_ID_NOT_FOUND, id));
    }
}
