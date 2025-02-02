package com.moviehouse.exceptions.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionMessageConstant {
    public static final String EMAIL_ALREADY_EXISTS = "User with email='%s' is already in use";

    public static final String USER_BY_EMAIL_NOT_FOUND = "User with email='%s' not found";
    public static final String USER_BY_ID_NOT_FOUND = "User with id='%s' not found";
    public static final String ROLE_BY_NAME_NOT_FOUND = "Role with name='%s' not found";

    public static final String EMPTY_PASSWORD = "Password is required";
    public static final String EMPTY_EMAIL = "Email is required";
    public static final String EMPTY_NAME = "Name is required";
    public static final String EMPTY_ROLE = "Role is required";

    public static final String INCORRECT_NAME_FORMAT = "Name must contain only letters and a single space between words";
    public static final String INCORRECT_PASSWORD_FORMAT = "Password must be 8-32 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character (@$!%*?&)";

    public static final String INVALID_EMAIL_FORMAT = "Invalid email format";
    public static final String INVALID_LOGIN_DATA = "Invalid username or password";
    public static final String INVALID_TOKEN = "Invalid token";

    public static final String NAME_SIZE_EXCEEDED = "Name must not exceed 50 characters";
    public static final String PASSWORD_MISMATCH = "Passwords don't match";
    public static final String TOKEN_EXPIRED = "Token expired, please log in again";
    public static final String ACCESS_DENIED = "Forbidden: You don't have permission to access this resource";

    public static final String AUTHENTICATION_ERROR = "Authentication error";
    public static final String SERVER_ERROR = "Internal server error: ";
}
