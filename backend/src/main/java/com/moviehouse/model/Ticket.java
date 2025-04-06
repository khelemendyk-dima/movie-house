package com.moviehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = EMPTY_BOOKING_ID)
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotNull(message = EMPTY_SESSION_ID)
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private MovieSession session;

    @NotNull(message = EMPTY_SEAT_ID)
    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private boolean used;
}
