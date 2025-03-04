package com.moviehouse.service.impl;

import com.moviehouse.dto.PaymentRequest;
import com.moviehouse.exceptions.BookingNotFoundException;
import com.moviehouse.exceptions.ServiceException;
import com.moviehouse.model.*;
import com.moviehouse.repository.BookingRepository;
import com.moviehouse.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.moviehouse.exceptions.constant.ExceptionMessageConstant.*;
import static java.lang.String.format;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    private final BookingRepository bookingRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public String createCheckoutSession(PaymentRequest paymentRequest) {
        Booking booking = findBookingById(paymentRequest.getBookingId());

        List<SessionCreateParams.LineItem> lineItems = booking.getTickets().stream()
                .map(ticket -> createLineItem(booking, ticket))
                .toList();

        SessionCreateParams params = buildSessionParams(paymentRequest, booking, lineItems);

        return createPaymentUrl(params);
    }

    @Transactional
    @Override
    public boolean verifyPayment(String payload, String signatureHeader) {
        try {

            Event event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                changeBookingStatusToPaid(event);
                return true;
            }
        } catch (SignatureVerificationException e) {
            throw new ServiceException(INVALID_STRIPE_SIGNATURE);
        }

        return false;
    }

    private Booking findBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    private SessionCreateParams.LineItem createLineItem(Booking booking, Ticket ticket) {
        MovieSession session = booking.getSession();
        Movie movie = session.getMovie();

        return SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(session.getPrice().longValue() * 100)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Movie Ticket (Seat: " + ticket.getSeat().getSeatNumber() + ", Row: " + ticket.getSeat().getRowNumber() + ")")
                                                .setDescription("Movie: " + movie.getTitle() +
                                                        ", Hall: " + session.getHall().getId() +
                                                        ", Start Time: " + session.getStartTime())
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    private SessionCreateParams buildSessionParams(PaymentRequest paymentRequest, Booking booking, List<SessionCreateParams.LineItem> lineItems) {
        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(paymentRequest.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(paymentRequest.getCancelUrl())
                .addAllLineItem(lineItems)
                .putMetadata("bookingId", booking.getId().toString())
                .build();
    }

    private static String createPaymentUrl(SessionCreateParams params) {
        try {
            return Session.create(params).getUrl();
        } catch (StripeException e) {
            throw new ServiceException(format(STRIPE_PAYMENT_FAILED, e.getMessage()));
        }
    }

    private void changeBookingStatusToPaid(Event event) {
        Session session = getStripeSession(event);

        Long bookingId = Long.parseLong(session.getMetadata().get("bookingId"));

        Booking booking = findBookingById(bookingId);
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);

        log.info("Booking with id={} marked as PAID", booking.getId());
    }

    private Session getStripeSession(Event event) {
        return (Session) event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new ServiceException(NULL_STRIPE_SESSION));
    }
}
