package com.moviehouse.service;

import com.moviehouse.dto.PaymentRequest;

public interface PaymentService {
    String createCheckoutSession(PaymentRequest paymentRequest);
    boolean verifyPayment(String payload, String signatureHeader);
}
