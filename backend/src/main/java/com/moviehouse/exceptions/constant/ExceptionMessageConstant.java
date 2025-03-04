package com.moviehouse.exceptions.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageConstant {
    public static final String EMAIL_ALREADY_EXISTS = "User with email='%s' is already in use";
    public static final String SEATS_ALREADY_BOOKED = "Seats already booked: ids=%s";
    public static final String INVALID_SEATS_FOR_HALL = "Some seats do not belong to the session's hall: sessionId=%s, hallId=%s, seatIds=%s";

    public static final String USER_BY_EMAIL_NOT_FOUND = "User with email='%s' not found";
    public static final String USER_BY_ID_NOT_FOUND = "User with id='%s' not found";
    public static final String ROLE_BY_NAME_NOT_FOUND = "Role with name='%s' not found";
    public static final String MOVIE_BY_ID_NOT_FOUND = "Movie with id='%s' not found";
    public static final String POSTER_NOT_FOUND = "Poster='%s' not found";
    public static final String MOVIE_SESSION_BY_ID_NOT_FOUND = "Movie session with id='%s' not found";
    public static final String HALL_BY_ID_NOT_FOUND = "Hall with id='%s' not found";
    public static final String GENRE_BY_NAME_NOT_FOUND = "Genre with name='%s' not found";
    public static final String SEAT_BY_IDS_NOT_FOUND = "Some seats do not exist: ids=%s";

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
    public static final String EMPTY_MOVIE_ID = "Movie ID is required";
    public static final String EMPTY_HALL_ID = "Hall ID is required";
    public static final String EMPTY_START_TIME = "Start time is required";
    public static final String EMPTY_START_DATE = "Start date is required";
    public static final String EMPTY_END_DATE = "Start date is required";
    public static final String EMPTY_PRICE = "Price is required";
    public static final String EMPTY_ROW_COUNT = "Row count is required";
    public static final String EMPTY_SEATS_PER_ROW = "Seats per row is required";
    public static final String EMPTY_HALL_NAME = "Hall name is required";
    public static final String EMPTY_ROW_NUMBER = "Row number is required";
    public static final String EMPTY_SEAT_NUMBER = "Seat number is required";
    public static final String EMPTY_MOVIE_GENRES = "Movie must have at least one genre";
    public static final String EMPTY_SESSION_ID = "Session ID is required";
    public static final String EMPTY_PHONE_NUMBER = "Phone number is required";
    public static final String EMPTY_SEAT_ID = "At least one seat must be selected";
    public static final String EMPTY_TOTAL_PRICE = "Total price is required";
    public static final String EMPTY_BOOKING_ID = "Booking ID is required";

    public static final String INCORRECT_NAME_FORMAT = "Name must contain only letters and a single space between words";
    public static final String INCORRECT_PASSWORD_FORMAT = "Password must be 8-32 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character (@$!%*?&)";

    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String INVALID_PHONE_FORMAT = "Invalid phone number format";
    public static final String INVALID_LOGIN_DATA = "Invalid username or password";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String INVALID_DURATION = "Duration must be at least 1 minute";
    public static final String INVALID_START_TIME = "Start time must be in the present or future";
    public static final String INVALID_START_DATE = "Start date must be in the present or future";
    public static final String INVALID_END_DATE = "End date must be in the future";
    public static final String INVALID_PRICE = "Price must be greater than zero";
    public static final String INVALID_ROW_COUNT = "Row count must be at least 1";
    public static final String INVALID_SEATS_PER_ROW = "Seats per row must be at least 1";
    public static final String INVALID_ROW_NUMBER = "Row number must be at least 1";
    public static final String INVALID_SEAT_NUMBER = "Seat number must be at least 1";
    public static final String INVALID_POSTER_URL = "Poster URL must be a valid URL. For instance, 'https://example.com/image.jpg'";

    public static final String NAME_SIZE_EXCEEDED = "Name must not exceed 50 characters";
    public static final String TITLE_SIZE_EXCEEDED = "Title must not exceed 100 characters";
    public static final String DESCRIPTION_SIZE_EXCEEDED = "Description must not exceed 500 characters";
    public static final String AGE_RATING_SIZE_EXCEEDED = "Age must not exceed 10 characters";
    public static final String HALL_NAME_SIZE_EXCEEDED = "Hall name must not exceed 50 characters";

    public static final String PASSWORD_MISMATCH = "Passwords don't match";
    public static final String TOKEN_EXPIRED = "Token expired, please log in again";
    public static final String ACCESS_DENIED = "Forbidden: You don't have permission to access this resource";
    public static final String FAILED_TO_UPLOAD_POSTER = "Failed to upload poster: %s";
    public static final String FAILED_TO_LOAD_POSTER = "Failed to load poster: %s";

    public static final String AUTHENTICATION_ERROR = "Authentication error";
    public static final String SERVER_ERROR = "Internal server error: ";
}
