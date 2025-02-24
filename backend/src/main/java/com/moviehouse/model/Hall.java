package com.moviehouse.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.EMPTY_HALL_NAME;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.HALL_NAME_SIZE_EXCEEDED;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.INVALID_ROW_COUNT;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.EMPTY_ROW_COUNT;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.EMPTY_SEATS_PER_ROW;
import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.INVALID_SEATS_PER_ROW;

@Data
@Entity
@Table(name = "halls")
@ToString(exclude = "seats")
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = EMPTY_HALL_NAME)
    @Size(max = 50, message = HALL_NAME_SIZE_EXCEEDED)
    private String name;

    @NotNull(message = EMPTY_ROW_COUNT)
    @Min(value = 1, message = INVALID_ROW_COUNT)
    @Column(name = "row_count", nullable = false)
    private Integer rowCount;

    @NotNull(message = EMPTY_SEATS_PER_ROW)
    @Min(value = 1, message = INVALID_SEATS_PER_ROW)
    @Column(name = "seats_per_row", nullable = false)
    private Integer seatsPerRow;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats;
}