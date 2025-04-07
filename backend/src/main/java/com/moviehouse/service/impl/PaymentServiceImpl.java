package com.moviehouse.service.impl;

import com.moviehouse.dto.PaymentRequest;
import com.moviehouse.exception.BookingAlreadyPaidException;
import com.moviehouse.exception.BookingNotFoundException;
import com.moviehouse.exception.ServiceException;
import com.moviehouse.model.*;
import com.moviehouse.repository.BookingRepository;
import com.moviehouse.service.EmailService;
import com.moviehouse.service.PaymentService;
import com.moviehouse.service.TicketService;
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

import static com.moviehouse.exception.constant.ExceptionMessageConstant.*;
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
    private final TicketService ticketService;
    private final EmailService emailService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public String createCheckoutSession(PaymentRequest paymentRequest) {
        log.info("Creating checkout session for bookingId: {}", paymentRequest.getBookingId());
        Booking booking = findBookingById(paymentRequest.getBookingId());

        List<SessionCreateParams.LineItem> lineItems = booking.getTickets().stream()
                .map(ticket -> createLineItem(booking, ticket))
                .toList();

        SessionCreateParams params = buildSessionParams(paymentRequest, booking, lineItems);

        String paymentUrl = createPaymentUrl(params);
        log.debug("Created payment URL: {}", paymentUrl);

        return paymentUrl;
    }

    @Transactional
    @Override
    public boolean verifyPayment(String payload, String signatureHeader) {
        log.info("Verifying payment with payload and signature header");

        try {
            Event event = Webhook.constructEvent(payload, signatureHeader, webhookSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                handleSuccessfulPayment(event);
                log.info("Payment session completed successfully");

                return true;
            }
        } catch (SignatureVerificationException e) {
            throw new ServiceException(INVALID_STRIPE_SIGNATURE);
        }

        return false;
    }

    private void handleSuccessfulPayment(Event event) {
        log.info("Handling successful payment eventId: {}", event.getId());

        Booking booking = changeBookingStatusToPaid(event);

        byte[] ticketsPdf = ticketService.generateTicketsPdf(booking.getTickets());
        ticketService.saveTicketsPdf(ticketsPdf, booking.getId());

        emailService.sendBookingConfirmation(booking.getEmail(), booking);
        log.info("Booking confirmation sent to email: {}", booking.getEmail());
    }

    private Booking findBookingById(Long bookingId) {
        log.info("Searching booking with id={}", bookingId);

        if (bookingRepository.existsByIdAndStatus(bookingId, BookingStatus.PAID)) {
            throw new BookingAlreadyPaidException(bookingId);
        }

        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    private SessionCreateParams.LineItem createLineItem(Booking booking, Ticket ticket) {
        MovieSession session = booking.getSession();
        Movie movie = session.getMovie();

        log.debug("Creating line item for ticket (Seat: {}, Row: {}) for movie: {}", ticket.getSeat().getSeatNumber(),
                ticket.getSeat().getRowNumber(), movie.getTitle());

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
        log.debug("Building session parameters for paymentRequest: {}", paymentRequest);

        return SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomerEmail(booking.getEmail())
                .setSuccessUrl(paymentRequest.getSuccessUrl() + "?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(paymentRequest.getCancelUrl())
                .addAllLineItem(lineItems)
                .putMetadata("bookingId", booking.getId().toString())
                .build();
    }

    private static String createPaymentUrl(SessionCreateParams params) {
        try {
            log.info("Creating payment session with Stripe");
            return Session.create(params).getUrl();
        } catch (StripeException e) {
            log.error("Stripe payment session creation failed: {}", e.getMessage());
            throw new ServiceException(format(STRIPE_PAYMENT_FAILED, e.getMessage()));
        }
    }

    @Transactional
    public Booking changeBookingStatusToPaid(Event event) {
        log.info("Changing booking status to PAID for eventId: {}", event.getId());

        Session session = getStripeSession(event);
        Long bookingId = Long.parseLong(session.getMetadata().get("bookingId"));

        Booking booking = findBookingById(bookingId);
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);

        log.info("Booking with id={} marked as PAID", booking.getId());

        return booking;
    }

    private Session getStripeSession(Event event) {
        log.debug("Retrieving Stripe session from eventId: {}", event.getId());

        return (Session) event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new ServiceException(NULL_STRIPE_SESSION));
    }
}
