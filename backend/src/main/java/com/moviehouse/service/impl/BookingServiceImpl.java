package com.moviehouse.service.impl;

import com.moviehouse.dto.BookingDto;
import com.moviehouse.exception.*;
import com.moviehouse.model.*;
import com.moviehouse.repository.BookingRepository;
import com.moviehouse.repository.MovieSessionRepository;
import com.moviehouse.repository.SeatRepository;
import com.moviehouse.service.BookingService;
import com.moviehouse.util.ConvertorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Value("${booking.cleanup.expiration-minutes}")
    private int expirationMinutes;

    private final BookingRepository bookingRepository;
    private final MovieSessionRepository movieSessionRepository;
    private final SeatRepository seatRepository;
    private final ConvertorUtil convertor;

    @Transactional
    @Override
    public BookingDto createBooking(BookingDto bookingDto) {
        log.info("Creating booking for sessionId={} with seats={}", bookingDto.getSessionId(), bookingDto.getSeatIds());

        MovieSession session = findMovieSessionById(bookingDto.getSessionId());
        List<Seat> seats = validateSeats(bookingDto.getSeatIds(), session);

        Booking booking = createBookingEntity(bookingDto, session, seats);
        bookingRepository.save(booking);

        log.info("Booking created successfully with id={}", booking.getId());

        return convertor.toBookingDto(booking);
    }

    @Override
    public boolean isBookingPaid(Long bookingId) {
        log.debug("Checking if booking with id={} is paid", bookingId);

        boolean isPaid = bookingRepository.existsByIdAndStatus(bookingId, BookingStatus.PAID);

        log.debug("Booking with id={} isPaid={}", bookingId, isPaid);

        return isPaid;
    }

    @Transactional
    @Override
    public void removeExpiredBookings() {
        log.info("Removing expired unpaid bookings older than {} minutes", expirationMinutes);

        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(expirationMinutes);
        int deletedCount = bookingRepository.deleteExpiredBookings(tenMinutesAgo, BookingStatus.PENDING);

        if (deletedCount > 0) {
            log.info("Deleted {} expired unpaid bookings.", deletedCount);
        } else {
            log.debug("No expired unpaid bookings found to delete.");
        }
    }

    private MovieSession findMovieSessionById(Long id) {
        log.debug("Finding movie session with id={}", id);

        return movieSessionRepository.findById(id)
                .orElseThrow(() -> new MovieSessionNotFoundException(id));
    }

    private List<Seat> validateSeats(List<Long> seatIds, MovieSession session) {
        log.debug("Validating seats={} for sessionId={}", seatIds, session.getId());

        List<Seat> seats = seatRepository.findAllById(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new SeatNotFoundException(seatIds);
        }

        boolean invalidSeats = seats.stream().anyMatch(seat -> !seat.getHall().getId().equals(session.getHall().getId()));
        if (invalidSeats) {
            throw new InvalidSeatForHallException(session.getId(), session.getHall().getId(), seatIds);
        }

        if (seatRepository.areSeatsTaken(session.getId(), seatIds)) {
            throw new SeatAlreadyBookedException(seatIds);
        }

        log.debug("All seats are valid and available for sessionId={}", session.getId());

        return seats;
    }

    private Booking createBookingEntity(BookingDto bookingDto, MovieSession session, List<Seat> seats) {
        log.debug("Creating booking entity for sessionId={} with seats={}", session.getId(), seats);

        Booking booking = new Booking();
        booking.setName(bookingDto.getName());
        booking.setEmail(bookingDto.getEmail());
        booking.setPhone(bookingDto.getPhone());
        booking.setSession(session);
        booking.setStatus(BookingStatus.PENDING);

        List<Ticket> tickets = seats.stream()
                .map(seat -> createTicketEntity(session, seat, booking))
                .toList();

        booking.setTickets(tickets);
        booking.setTotalPrice(calculateTotalPrice(tickets, session));

        log.debug("Booking entity created successfully for sessionId={}", session.getId());

        return booking;
    }

    private static Ticket createTicketEntity(MovieSession session, Seat seat, Booking booking) {
        log.debug("Creating ticket for seatId={} in sessionId={}", seat.getId(), session.getId());

        Ticket ticket = new Ticket();
        ticket.setSession(session);
        ticket.setBooking(booking);
        ticket.setSeat(seat);

        return ticket;
    }

    private BigDecimal calculateTotalPrice(List<Ticket> tickets, MovieSession session) {
        BigDecimal totalPrice = BigDecimal.valueOf(tickets.size()).multiply(session.getPrice());

        log.debug("Calculated total price={} for {} tickets in sessionId={}", totalPrice, tickets.size(), session.getId());

        return totalPrice;
    }
}
