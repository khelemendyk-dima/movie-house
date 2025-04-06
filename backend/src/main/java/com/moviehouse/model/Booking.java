package com.moviehouse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;
import static com.moviehouse.util.constant.RegexConstant.PHONE_NUMBER_REGEX;

@Data
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = EMPTY_SESSION_ID)
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private MovieSession session;

    @NotBlank(message = EMPTY_NAME)
    @Column(nullable = false)
    private String name;

    @NotBlank(message = EMPTY_EMAIL)
    @Email(message = INVALID_EMAIL_FORMAT)
    @Column(nullable = false)
    private String email;

    @NotBlank(message = EMPTY_PHONE_NUMBER)
    @Pattern(regexp = PHONE_NUMBER_REGEX, message = INVALID_PHONE_FORMAT)
    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @NotNull(message = EMPTY_TOTAL_PRICE)
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;
}
