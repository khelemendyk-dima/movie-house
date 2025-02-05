package com.moviehouse.exceptions.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageConstant {
    public static final String EMAIL_ALREADY_EXISTS = "User with email='%s' is already in use";

    public static final String USER_BY_EMAIL_NOT_FOUND = "User with email='%s' not found";
    public static final String USER_BY_ID_NOT_FOUND = "User with id='%s' not found";
    public static final String ROLE_BY_NAME_NOT_FOUND = "Role with name='%s' not found";
    public static final String MOVIE_BY_ID_NOT_FOUND = "Movie with id='%s' not found";
    public static final String POSTER_NOT_FOUND = "Poster='%s' not found";

    public static final String EMPTY_PASSWORD = "Password is required";
    public static final String EMPTY_EMAIL = "Email is required";
    public static final String EMPTY_NAME = "Name is required";
    public static final String EMPTY_ROLE = "Role is required";
    public static final String EMPTY_TITLE = "Title is required";
    public static final String EMPTY_DESCRIPTION = "Description is required";
    public static final String EMPTY_DURATION = "Duration is required";
    public static final String EMPTY_AGE_RATING = "Age rating is required";
    public static final String EMPTY_RELEASE_DATE = "Release date is required";
    public static final String EMPTY_POSTER_URL = "Poster URL is required";

    public static final String INCORRECT_NAME_FORMAT = "Name must contain only letters and a single space between words";
    public static final String INCORRECT_PASSWORD_FORMAT = "Password must be 8-32 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character (@$!%*?&)";

    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String INVALID_LOGIN_DATA = "Invalid username or password";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_DURATION = "Duration must be at least 1 minute";

    public static final String NAME_SIZE_EXCEEDED = "Name must not exceed 50 characters";
    public static final String TITLE_SIZE_EXCEEDED = "Title must not exceed 100 characters";
    public static final String DESCRIPTION_SIZE_EXCEEDED = "Description must not exceed 500 characters";
    public static final String AGE_RATING_SIZE_EXCEEDED = "Age must not exceed 10 characters";

    public static final String PASSWORD_MISMATCH = "Passwords don't match";
    public static final String TOKEN_EXPIRED = "Token expired, please log in again";
    public static final String ACCESS_DENIED = "Forbidden: You don't have permission to access this resource";
    public static final String FAILED_TO_UPLOAD_POSTER = "Failed to upload poster: %s";
    public static final String FAILED_TO_LOAD_POSTER = "Failed to load poster: %s";

    public static final String AUTHENTICATION_ERROR = "Authentication error";
    public static final String SERVER_ERROR = "Internal server error: ";
}
