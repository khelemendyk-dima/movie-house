package com.moviehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatStatusDto {
    private Long seatId;
    private Integer rowNumber;
    private Integer seatNumber;
    private String status;
}