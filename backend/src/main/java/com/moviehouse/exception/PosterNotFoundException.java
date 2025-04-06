package com.moviehouse.exception;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.POSTER_NOT_FOUND;
import static java.lang.String.format;

public class PosterNotFoundException extends NotFoundException {
    public PosterNotFoundException(String filename) {
        super(format(POSTER_NOT_FOUND, filename));
    }
}
