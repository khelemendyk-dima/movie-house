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

@Data
@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Hall ID must not be null")
    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @NotNull(message = "Row number must be specified")
    @Min(value = 1, message = "Row number must be at least 1")
    private Integer rowNumber;

    @NotNull(message = "Seat number must be specified")
    @Min(value = 1, message = "Seat number must be at least 1")
    private Integer seatNumber;
}
