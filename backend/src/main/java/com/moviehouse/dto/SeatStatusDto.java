package com.moviehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SeatStatusDto {
    private Long seatId;
    private int rowNumber;
    private int seatNumber;
    private String status;
}