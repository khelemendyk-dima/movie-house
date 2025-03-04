package com.moviehouse.controller;

import com.moviehouse.dto.PaymentRequest;
import com.moviehouse.service.PaymentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestBody @Valid PaymentRequest request) {
        String sessionUrl = paymentService.createCheckoutSession(request);

        return ResponseEntity.ok(sessionUrl);
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody @NotNull String payload,
                                                @RequestHeader("Stripe-Signature") String signatureHeader) {
        if (paymentService.verifyPayment(payload, signatureHeader)) {
            return ResponseEntity.ok("Payment verified successfully");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Payment verification failed");
    }
}
