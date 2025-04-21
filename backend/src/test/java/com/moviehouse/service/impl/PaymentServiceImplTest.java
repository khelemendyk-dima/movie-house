package com.moviehouse.service.impl;

import com.moviehouse.dto.PaymentRequest;
import com.moviehouse.exception.BookingAlreadyPaidException;
import com.moviehouse.exception.BookingNotFoundException;
import com.moviehouse.exception.ServiceException;
import com.moviehouse.model.*;
import com.moviehouse.repository.BookingRepository;
import com.moviehouse.service.EmailService;
import com.moviehouse.service.TicketService;
import com.moviehouse.config.TestDataFactory;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private Event event;

    @Mock
    private Session session;

    @Mock
    private EventDataObjectDeserializer deserializer;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TicketService ticketService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequest paymentRequest;
    private Booking booking;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(paymentService, "stripeSecretKey", "test-secret-key");
        ReflectionTestUtils.setField(paymentService, "webhookSecret", "test-webhook-secret");

        Movie movie = TestDataFactory.createMovie();
        Hall hall = TestDataFactory.createHall();
        MovieSession movieSession = TestDataFactory.createMovieSession(movie, hall);
        Ticket ticket = TestDataFactory.createTicket();

        booking = TestDataFactory.createBooking(BookingStatus.PENDING, movieSession);
        booking.setTickets(List.of(ticket));

        paymentRequest = TestDataFactory.createPaymentRequest();
    }

    @Test
    void createCheckoutSession_shouldReturnUrl_whenValidRequest() {
        when(session.getUrl()).thenReturn("http://payment.url");
        when(bookingRepository.existsByIdAndStatus(1L, BookingStatus.PAID)).thenReturn(false);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        try (MockedStatic<Session> sessionMockedStatic = mockStatic(Session.class)) {
            sessionMockedStatic.when(() -> Session.create(any(SessionCreateParams.class)))
                    .thenReturn(session);

            String url = paymentService.createCheckoutSession(paymentRequest);

            assertThat(url).isEqualTo("http://payment.url");
        }
    }

    @Test
    void createCheckoutSession_shouldThrowException_whenBookingNotFound() {
        when(bookingRepository.existsByIdAndStatus(1L, BookingStatus.PAID)).thenReturn(false);
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> paymentService.createCheckoutSession(paymentRequest));
    }

    @Test
    void createCheckoutSession_shouldThrowException_whenBookingAlreadyPaid() {
        when(bookingRepository.existsByIdAndStatus(1L, BookingStatus.PAID)).thenReturn(true);

        assertThrows(BookingAlreadyPaidException.class, () -> paymentService.createCheckoutSession(paymentRequest));
    }

    @Test
    void verifyPayment_shouldReturnTrue_whenSessionCompleted() {
        String payload = "payload";
        String signature = "signature";
        String webhookSecret = "test-webhook-secret";

        when(event.getType()).thenReturn("checkout.session.completed");
        when(event.getId()).thenReturn("evt_123");

        try (MockedStatic<Webhook> webhookMockedStatic = mockStatic(Webhook.class)) {
            webhookMockedStatic.when(() ->
                    Webhook.constructEvent(payload, signature, webhookSecret)
            ).thenReturn(event);

            when(event.getDataObjectDeserializer()).thenReturn(deserializer);
            when(deserializer.getObject()).thenReturn(Optional.of(session));
            when(session.getMetadata()).thenReturn(Map.of("bookingId", "1"));

            when(bookingRepository.existsByIdAndStatus(1L, BookingStatus.PAID)).thenReturn(false);
            when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

            byte[] pdf = new byte[]{1, 2, 3};
            when(ticketService.generateTicketsPdf(anyList())).thenReturn(pdf);

            boolean result = paymentService.verifyPayment(payload, signature);

            assertThat(result).isTrue();
            verify(bookingRepository).save(any());
            verify(emailService).sendBookingConfirmation(eq("john.doe@example.com"), any());
        }
    }

    @Test
    void verifyPayment_shouldReturnFalse_whenNotCompleted() {
        String payload = "payload";
        String signature = "signature";
        String webhookSecret = "test-webhook-secret";

        when(event.getType()).thenReturn("other.event");

        try (MockedStatic<Webhook> webhookMockedStatic = mockStatic(Webhook.class)) {
            webhookMockedStatic.when(() ->
                    Webhook.constructEvent(payload, signature, webhookSecret)
            ).thenReturn(event);

            boolean result = paymentService.verifyPayment(payload, signature);

            assertThat(result).isFalse();
        }
    }

    @Test
    void changeBookingStatusToPaid_shouldUpdateStatus() {
        when(event.getId()).thenReturn("evt_123");
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);
        when(deserializer.getObject()).thenReturn(Optional.of(session));
        when(session.getMetadata()).thenReturn(Map.of("bookingId", "1"));

        when(bookingRepository.existsByIdAndStatus(1L, BookingStatus.PAID)).thenReturn(false);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = paymentService.changeBookingStatusToPaid(event);

        assertThat(result.getStatus()).isEqualTo(BookingStatus.PAID);
        verify(bookingRepository).save(result);
    }

    @Test
    void getStripeSession_shouldThrowException_whenSessionNull() {
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);
        when(deserializer.getObject()).thenReturn(Optional.empty());

        assertThrows(ServiceException.class, () -> paymentService.changeBookingStatusToPaid(event));
    }
}
