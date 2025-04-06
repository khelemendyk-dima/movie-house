package com.moviehouse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = EMPTY_HALL_ID)
    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotNull(message = EMPTY_ROW_NUMBER)
    @Min(value = 1, message = INVALID_ROW_NUMBER)
    private Integer rowNumber;

    @NotNull(message = EMPTY_SEAT_NUMBER)
    @Min(value = 1, message = INVALID_SEAT_NUMBER)
    private Integer seatNumber;
}
