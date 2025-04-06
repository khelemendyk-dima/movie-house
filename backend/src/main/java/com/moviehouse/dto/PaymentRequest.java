package com.moviehouse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;

@Data
public class PaymentRequest {
    @NotNull(message = EMPTY_BOOKING_ID)
    private Long bookingId;

    @NotBlank(message = EMPTY_SUCCESS_URL)
    private String successUrl;

    @NotBlank(message = EMPTY_CANCEL_URL)
    private String cancelUrl;
}