package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.MOVIE_BY_ID_NOT_FOUND;
import static java.lang.String.format;

public class MovieNotFoundException extends NotFoundException {
    public MovieNotFoundException(Long id) {
        super(format(MOVIE_BY_ID_NOT_FOUND, id));
    }
}
