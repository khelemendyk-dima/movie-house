package com.moviehouse.exceptions;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.GENRE_BY_NAME_NOT_FOUND;
import static java.lang.String.format;

public class GenreNotFoundException extends NotFoundException {
    public GenreNotFoundException(String genreName) {
        super(format(GENRE_BY_NAME_NOT_FOUND, genreName));
    }
}