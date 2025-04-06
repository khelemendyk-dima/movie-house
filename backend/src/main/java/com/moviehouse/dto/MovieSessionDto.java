package com.moviehouse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
public class MovieSessionDto {
    private Long id;

    @NotNull(message = EMPTY_MOVIE_ID)
    private Long movieId;

    @NotNull(message = EMPTY_HALL_ID)
    private Long hallId;

    @NotNull(message = EMPTY_START_TIME)
    @FutureOrPresent(message = INVALID_START_TIME)
    private LocalDateTime startTime;

    @NotNull(message = EMPTY_PRICE)
    @DecimalMin(value = "0.0", inclusive = false, message = INVALID_PRICE)
    private BigDecimal price;
}