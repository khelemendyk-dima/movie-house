package com.moviehouse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
public class MovieSessionRegistrationDto {
    @NotNull(message = EMPTY_MOVIE_ID)
    private Long movieId;

    @NotNull(message = EMPTY_HALL_ID)
    private Long hallId;

    @NotNull(message = EMPTY_START_TIME)
    private LocalTime startTime;

    @NotNull(message = EMPTY_START_DATE)
    @FutureOrPresent(message = INVALID_START_DATE)
    private LocalDate startDate;

    @NotNull(message = EMPTY_END_DATE)
    @FutureOrPresent(message = INVALID_END_DATE)
    private LocalDate endDate;

    @NotNull(message = EMPTY_PRICE)
    @DecimalMin(value = "0.0", inclusive = false, message = INVALID_PRICE)
    private BigDecimal price;
}
