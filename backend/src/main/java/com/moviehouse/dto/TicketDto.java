package com.moviehouse.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDto {
    private String username;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private int rowNumber;
    private int seatNumber;
    private boolean used;
}
