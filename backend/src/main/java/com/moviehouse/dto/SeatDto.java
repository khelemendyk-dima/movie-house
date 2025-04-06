package com.moviehouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
public class SeatDto {
    private Long id;

    @NotNull(message = EMPTY_ROW_NUMBER)
    @Min(value = 1, message = INVALID_ROW_NUMBER)
    private Integer rowNumber;

    @NotNull(message = EMPTY_SEAT_NUMBER)
    @Min(value = 1, message = INVALID_SEAT_NUMBER)
    private Integer seatNumber;
}
