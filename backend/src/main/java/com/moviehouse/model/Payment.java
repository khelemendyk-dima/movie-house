package com.moviehouse.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GenerationType;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Booking ID must not be null")
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @NotBlank(message = "Payment status is required")
    @Pattern(regexp = "^(PENDING|COMPLETED|FAILED|CANCELLED)$",
            message = "Payment status must be one of: PENDING, COMPLETED, FAILED, CANCELLED")
    private String status;

    @NotBlank(message = "Payment method is required")
    @Pattern(regexp = "^(CREDIT_CARD|PAYPAL|STRIPE|CASH)$",
            message = "Payment method must be one of: CREDIT_CARD, PAYPAL, STRIPE, CASH")
    private String paymentMethod;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
