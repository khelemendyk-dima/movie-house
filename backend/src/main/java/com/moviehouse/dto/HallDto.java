package com.moviehouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_NAME;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_ROW_COUNT;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.EMPTY_SEATS_PER_ROW;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.INVALID_ROW_COUNT;
import static com.moviehouse.exception.constant.ExceptionMessageConstant.INVALID_SEATS_PER_ROW;

@Data
public class HallDto {
    private Long id;

    @NotBlank(message = EMPTY_NAME)
    private String name;

    @NotNull(message = EMPTY_ROW_COUNT)
    @Min(value = 1, message = INVALID_ROW_COUNT)
    private Integer rowCount;

    @NotNull(message = EMPTY_SEATS_PER_ROW)
    @Min(value = 1, message = INVALID_SEATS_PER_ROW)
    private Integer seatsPerRow;

    private List<SeatDto> seats;
}