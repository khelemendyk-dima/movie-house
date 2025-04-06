package com.moviehouse.dto;

import com.moviehouse.model.BookingStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;
import static com.moviehouse.util.constant.RegexConstant.PHONE_NUMBER_REGEX;

@Data
public class BookingDto {
    private Long bookingId;

    @NotNull(message = EMPTY_SESSION_ID)
    private Long sessionId;

    @NotBlank(message = EMPTY_NAME)
    private String name;

    @NotBlank(message = EMPTY_EMAIL)
    @Email(message = INVALID_EMAIL_FORMAT)
    private String email;

    @NotBlank(message = EMPTY_PHONE_NUMBER)
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = INVALID_PHONE_FORMAT)
    private String phone;

    @NotEmpty(message = EMPTY_SEAT_ID)
    private List<Long> seatIds;

    private String totalPrice;
    private BookingStatus status;
}