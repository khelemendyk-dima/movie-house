package com.moviehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
@Entity
@Table(name = "sessions")
public class MovieSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = EMPTY_MOVIE_ID)
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @NotNull(message = EMPTY_HALL_ID)
    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotNull(message = EMPTY_START_TIME)
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull(message = EMPTY_PRICE)
    @DecimalMin(value = "0.0", inclusive = false, message = INVALID_PRICE)
    private BigDecimal price;
}