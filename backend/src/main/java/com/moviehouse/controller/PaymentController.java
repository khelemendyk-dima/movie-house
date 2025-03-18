package com.moviehouse.controller;

import com.moviehouse.dto.PaymentRequest;
import com.moviehouse.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Endpoints for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Create checkout session for payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created checkout session."),
            @ApiResponse(responseCode = "400", description = "Validation error in input data."),
            @ApiResponse(responseCode = "404", description = "Booking not found."),
            @ApiResponse(responseCode = "409", description = "Booking conflict, e.g. already paid or seat booked")
    })
    @PostMapping("/checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestBody @Valid PaymentRequest request) {
        String sessionUrl = paymentService.createCheckoutSession(request);

        return ResponseEntity.ok(sessionUrl);
    }

    @Operation(summary = "Handle Stripe webhook for payment verification")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment verified successfully."),
            @ApiResponse(responseCode = "401", description = "Unauthorized access - Payment verification failed.")
    })
    @PostMapping("/stripe-webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody @NotNull String payload,
                                                @RequestHeader("Stripe-Signature") String signatureHeader) {
        if (paymentService.verifyPayment(payload, signatureHeader)) {
            return ResponseEntity.ok("Payment verified successfully");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Payment verification failed");
    }
}
