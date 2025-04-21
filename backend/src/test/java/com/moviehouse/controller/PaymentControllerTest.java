package com.moviehouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviehouse.config.TestDataFactory;
import com.moviehouse.dto.PaymentRequest;
import com.moviehouse.exception.BookingAlreadyPaidException;
import com.moviehouse.exception.BookingNotFoundException;
import com.moviehouse.security.JwtAuthFilter;
import com.moviehouse.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCheckoutSession_shouldReturnUrl() throws Exception {
        PaymentRequest request = TestDataFactory.createPaymentRequest();
        String mockUrl = "https://checkout.stripe.com/session/test-session";

        Mockito.when(paymentService.createCheckoutSession(any())).thenReturn(mockUrl);

        mockMvc.perform(post("/api/payments/checkout-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(mockUrl));
    }

    @Test
    void createCheckoutSession_shouldReturn400_whenInvalidRequest() throws Exception {
        PaymentRequest request = new PaymentRequest();

        mockMvc.perform(post("/api/payments/checkout-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCheckoutSession_shouldReturnBookingNotFound() throws Exception {
        PaymentRequest request = TestDataFactory.createPaymentRequest();

        Mockito.when(paymentService.createCheckoutSession(any()))
                .thenThrow(BookingNotFoundException.class);

        mockMvc.perform(post("/api/payments/checkout-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCheckoutSession_shouldReturnBookingConflict() throws Exception {
        PaymentRequest request = TestDataFactory.createPaymentRequest();

        Mockito.when(paymentService.createCheckoutSession(any()))
                .thenThrow(BookingAlreadyPaidException.class);

        mockMvc.perform(post("/api/payments/checkout-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void handleWebhook_shouldReturnSuccess() throws Exception {
        String payload = "{\"type\": \"checkout.session.completed\"}";
        String signature = "test-signature";

        Mockito.when(paymentService.verifyPayment(payload, signature)).thenReturn(true);

        mockMvc.perform(post("/api/payments/stripe-webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment verified successfully"));
    }

    @Test
    void handleWebhook_shouldReturn401() throws Exception {
        String payload = "{\"type\": \"checkout.session.failed\"}";
        String signature = "invalid-signature";

        Mockito.when(paymentService.verifyPayment(payload, signature)).thenReturn(false);

        mockMvc.perform(post("/api/payments/stripe-webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Stripe-Signature", signature)
                        .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Payment verification failed"));
    }
}
