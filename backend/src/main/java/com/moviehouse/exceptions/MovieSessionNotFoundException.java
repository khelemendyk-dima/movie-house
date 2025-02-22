package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.MOVIE_SESSION_BY_ID_NOT_FOUND;
import static java.lang.String.format;

public class MovieSessionNotFoundException extends NotFoundException {
    public MovieSessionNotFoundException(Long id) {
        super(format(MOVIE_SESSION_BY_ID_NOT_FOUND, id));
    }
}
